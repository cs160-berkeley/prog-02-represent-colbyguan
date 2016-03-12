package com.example.colby.represent;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = BuildConfig.TWITTER_KEY;
    private static final String TWITTER_SECRET = BuildConfig.TWITTER_SECRET;

    private GoogleApiClient mGoogleApiClient;

    Button zipcodeButton;
    Button locationButton;
    EditText zipcodeInput;
    ImageView img1, img2, img3;
    int zipcode = 95070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        RepCollection.getInstance().setContext(this);
        CustomFont.init(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zipcodeButton = (Button) findViewById(R.id.buttonZipcode);
        locationButton = (Button) findViewById(R.id.buttonLocation);
        zipcodeInput = (EditText) findViewById(R.id.zipcode);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img1.setImageResource(R.drawable.initial_profile);
        img2.setImageResource(R.drawable.initial_profile);
        img3.setImageResource(R.drawable.initial_profile);

        zipcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zipcodeInput.getText().toString().length() == 0) {
                    zipcodeInput.requestFocus();
                } else {
                    LocationActivity.this.loadRepsWithZip();
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationActivity.this.loadRepsWithLocation();
            }
        });

        zipcodeInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    LocationActivity.this.loadRepsWithZip();
                }
                return true;
            }
        });

        TextView title = (TextView) findViewById(R.id.locationTitle);
        title.setTypeface(CustomFont.bold);
        zipcodeButton.setTypeface(CustomFont.bold);
        locationButton.setTypeface(CustomFont.bold);

        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey("random") && intent.getBooleanExtra("random", false) == true) {
            Log.d("LocationActivity", "reloading with random zip");
            double latitude = intent.getDoubleExtra("lat", PhoneListenerService.LAT_LOWER);
            double longitude = intent.getDoubleExtra("long", PhoneListenerService.LONG_LOWER);
            Location randomLoc = new Location("");
            randomLoc.setLatitude(latitude);
            randomLoc.setLongitude(longitude);
            RepCollection.getInstance().setCountyFromLocation(latitude, longitude);
            RepCollection.getInstance().asyncStartWithLocation(this, randomLoc);
        }
    }

    private void loadRepsWithZip() {
        img1.setImageResource(R.drawable.loading_profile);
        img2.setImageResource(R.drawable.loading_profile);
        img3.setImageResource(R.drawable.loading_profile);

        try {
            zipcode = Integer.parseInt(zipcodeInput.getText().toString());
            RepCollection.getInstance().setCountyFromLocation(zipcode);
            RepCollection.getInstance().asyncStartWithZip(this, zipcode);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid ZIP code", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void loadRepsWithLocation() {
        Location lastLocation;
        img1.setImageResource(R.drawable.loading_profile);
        img2.setImageResource(R.drawable.loading_profile);
        img3.setImageResource(R.drawable.loading_profile);

        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                RepCollection.getInstance().setCountyFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
            RepCollection.getInstance().asyncStartWithLocation(this, lastLocation);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Requires location permissions", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void updateImage(int index) {
        if (index > 2 || index >= RepCollection.getInstance().size()) {
            return;
        }
        switch(index) {
            case 0:
                img1.setImageBitmap(RepCollection.getInstance().get(index).bitmap);
                break;
            case 1:
                img2.setImageBitmap(RepCollection.getInstance().get(index).bitmap);
                break;
            case 2:
                img3.setImageBitmap(RepCollection.getInstance().get(index).bitmap);
                break;
            default: break;
        }
    }

    public void transitionWithReps() {
        Intent intent = new Intent(this, RepsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
