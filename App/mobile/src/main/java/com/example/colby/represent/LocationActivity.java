package com.example.colby.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    Button zipcodeButton;
    Button locationButton;
    EditText zipcodeInput;
    int zipcode = 95070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zipcodeButton = (Button) findViewById(R.id.buttonZipcode);
        locationButton = (Button) findViewById(R.id.buttonLocation);
        zipcodeInput = (EditText) findViewById(R.id.zipcode);

        zipcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zipcodeInput.getText().toString().length() == 0) {
                    zipcodeInput.requestFocus();
                } else {
                    LocationActivity.this.loadReps();
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipcodeInput.setText("95070");
                LocationActivity.this.loadReps();
            }
        });

        zipcodeInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    LocationActivity.this.loadReps();
                }
                return true;
            }
        });
    }

    private void loadReps() {
        try {
            zipcode = Integer.parseInt(zipcodeInput.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid ZIP code", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RepsActivity.class);
        intent.putExtra("zipcode", zipcode);
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
}
