package com.example.colby.represent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

/**
 * Created by colby on 3/10/16.
 * Backend singleton
 */
public class RepCollection {
    private static RepCollection instance = null;

    private ArrayList<Rep> reps;
    private Location location;
    private int zipcode;

    private int received = 0;
    private Context context;

    final String SUNLIGHT_LOCATION_BASE = "http://congress.api.sunlightfoundation.com/legislators/locate?";
    final String SUNLIGHT_COMMITTEES_BASE = "http://congress.api.sunlightfoundation.com/committees?";
    final String SUNLIGHT_BILLS_BASE = "http://congress.api.sunlightfoundation.com/bills?";
    final String GEOCODE_BASE = "https://maps.googleapis.com/maps/api/geocode/json?";
    final String GOOGLE_KEY = BuildConfig.GOOGLE_KEY;
    final String SUNLIGHT_KEY = BuildConfig.SUNLIGHT_KEY;

    JSONObject votes;
    public double dVotes = 0;
    public double rVotes = 0;
    public String county;
    public String state;

    private RepCollection() {
        reps = new ArrayList<>();
    }

    public static RepCollection getInstance() {
        if (instance == null) {
            instance = new RepCollection();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        try {
            InputStream stream = context.getResources().getAssets().open("votes.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String jsonString = new String(buffer, "UTF-8");
            votes = new JSONObject(jsonString);
        } catch (IOException e) {
            votes = null;
        } catch (JSONException e) {
            votes = null;
        }
    }

    public Rep get(int index) {
        return reps.get(index);
    }

    public int size() {
        return reps.size();
    }

    private void resetMetadata() {
        received = 0;
        reps.clear();
        dVotes = 0;
        rVotes = 0;
    }

    public void setCountyFromLocation(int zipcode) {
        String uri = GEOCODE_BASE + "address=" + String.format("%05d", zipcode) + "&key=" + GOOGLE_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.optJSONArray("results");
                    if (results == null) {
                        return;
                    }
                    JSONObject root = results.getJSONObject(0);
                    JSONArray components = root.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject comp = components.getJSONObject(i);
                        JSONArray types = comp.getJSONArray("types");

                        for (int j = 0; j < types.length(); j++) {
                            String type = types.getString(j);
                            if (type.equals("administrative_area_level_2")) {
                                county = comp.getString("long_name");
                            } else if (type.equals("administrative_area_level_1")) {
                                state = comp.getString("short_name");
                            }
                        }
                    }

                    String key = county + ", " + state;
                    if(votes.has(key))
                    {
                        JSONObject voteInfo = votes.getJSONObject(key);
                        dVotes = voteInfo.getDouble("obama");
                        rVotes = voteInfo.getDouble("romney");
                    }
                    Log.d("Geocode",county+" "+state+" "+dVotes+" "+rVotes);
                } catch (JSONException e) {
                    return;
                }
            }
        });
    }

    public void setCountyFromLocation(double latitude, double longitude) {
        String uri = GEOCODE_BASE + "latlng=" + latitude + "," + longitude + "&key=" + GOOGLE_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.optJSONArray("results");
                    if (results == null) {
                        return;
                    }
                    JSONObject root = results.optJSONObject(0);
                    JSONArray components = root.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject comp = components.getJSONObject(i);
                        JSONArray types = comp.getJSONArray("types");

                        for (int j = 0; j < types.length(); j++) {
                            String type = types.getString(j);
                            if (type.equals("administrative_area_level_2")) {
                                county = comp.getString("long_name");
                            } else if (type.equals("administrative_area_level_1")) {
                                state = comp.getString("short_name");
                            }
                        }
                    }

                    String key = county + ", " + state;
                    if(votes.has(key))
                    {
                        JSONObject voteInfo = votes.getJSONObject(key);
                        dVotes = voteInfo.getDouble("obama");
                        rVotes = voteInfo.getDouble("romney");
                    }
                    Log.d("Geocode",county+" "+state+" "+dVotes+" "+rVotes);
                } catch (JSONException e) {
                    return;
                }

            }
        });


    }

    public void asyncStartWithLocation(LocationActivity source, Location l) {
        if (l == null) {
            Toast.makeText(source, "Error getting location", Toast.LENGTH_SHORT).show();
            return;
        }
        location = l;
        String repUri = SUNLIGHT_LOCATION_BASE + "latitude=" + String.valueOf(l.getLatitude()) + "&longitude=" + String.valueOf(l.getLongitude()) + "&apikey=" + SUNLIGHT_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(repUri, null, new RepLocationResponseHandler(source));
    }

    public void asyncStartWithZip(LocationActivity source, int zip) {
        zipcode = zip;
        String repUri = SUNLIGHT_LOCATION_BASE + "zip=" + String.format("%05d", zip) + "&apikey=" + SUNLIGHT_KEY;
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("RepCollection", "async http get " + repUri);
        client.get(repUri, null, new RepLocationResponseHandler(source));
    }

    private class RepLocationResponseHandler extends JsonHttpResponseHandler {
        LocationActivity source;
        public RepLocationResponseHandler(LocationActivity source) {
            super();
            this.source = source;
            resetMetadata();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            final JSONArray res = response.optJSONArray("results");
            int count = response.optInt("count");
            if (count < 3) {
                criticalFailure(source);
                return;
            }
            for (int i = 0; i < res.length(); i++) {
                final int idx = i;
                JSONObject repObject;
                try {
                    repObject = res.getJSONObject(i);
                    String name = repObject.getString("first_name") + " " + repObject.getString("last_name");
                    String party = repObject.getString("party");
                    String chamber = repObject.getString("chamber");
                    final String twitter = repObject.getString("twitter_id");
                    String website = repObject.getString("website");
                    String email = repObject.getString("oc_email");
                    String end = repObject.getString("term_end");
                    String id = repObject.getString("bioguide_id");

                    final Rep thisRep = new Rep(name, party, chamber, email, website, twitter, end, id);
                    reps.add(thisRep);

                    AsyncHttpClient client = new AsyncHttpClient();
                    String committeesUri = SUNLIGHT_COMMITTEES_BASE + "member_ids=" + id + "&apikey=" + SUNLIGHT_KEY;
                    client.get(committeesUri, null, new RepCommitteesResponseHandler(source, idx, res.length(), thisRep));
                } catch (JSONException e) {
                    Log.d("RepCollection", "unable to JSON");
                    criticalFailure(source);
                }
            }
        }
    }

    private class RepCommitteesResponseHandler extends JsonHttpResponseHandler {
        LocationActivity source;
        int index, length;
        Rep rep;

        public RepCommitteesResponseHandler(LocationActivity source, int index, int length, Rep rep) {
            super();
            this.source = source;
            this.index = index;
            this.length = length;
            this.rep = rep;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            final JSONArray res = response.optJSONArray("results");
            ArrayList<String> cmts = new ArrayList<>();
            for (int i = 0; i < res.length(); i++) {
                try {
                    JSONObject cmtObject = res.getJSONObject(i);
                    cmts.add(cmtObject.getString("name"));
                } catch (JSONException e) {
                    criticalFailure(source);
                    continue;
                }
            }
            rep.setCmts(cmts);
            AsyncHttpClient client = new AsyncHttpClient();
            String billsUri = SUNLIGHT_BILLS_BASE + "sponsor_id=" + rep.id + "&apikey=" + SUNLIGHT_KEY;
            client.get(billsUri, null, new RepBillsResponseHandler(source, index, length, rep));
        }
    }

    private class RepBillsResponseHandler extends JsonHttpResponseHandler {
        LocationActivity source;
        int index, length;
        Rep rep;

        public RepBillsResponseHandler(LocationActivity source, int index, int length, Rep rep) {
            super();
            this.source = source;
            this.index = index;
            this.length = length;
            this.rep = rep;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            final JSONArray res = response.optJSONArray("results");
            ArrayList<String> bills = new ArrayList<>();
            for (int i = 0; i < res.length() && i < 3; i++) {
                try {
                    JSONObject billObject = res.getJSONObject(i);
                    String date, title;
                    date = billObject.getString("introduced_on");
                    if (billObject.isNull("short_title")) {
                        title = billObject.getString("official_title");
                    } else {
                        title = billObject.getString("short_title");
                    }
                    bills.add(date + " - " + title);
                } catch (JSONException e) {
                    criticalFailure(source);
                    continue;
                }
            }
            rep.setBills(bills);
            getTwitterDataOfRep(source, rep, index, length);
        }
    }

    private void getTwitterDataOfRep(LocationActivity src, Rep rep, int index, int len) {
        // Start Twitter Retrieval
        final LocationActivity source = src;
        final String twitter = rep.twitterId;
        final int idx = index;
        final int length = len;
        final Rep thisRep = rep;

        Log.d("RepCollection", "Start twitter guest login");
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                Log.d("RepCollection", "Start twitter retrieve");
                twitterApiClient.getStatusesService().userTimeline(null, twitter, 1, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        Tweet lastTweet = result.data.get(0);
                        thisRep.setLastTweet(lastTweet.text);
                        thisRep.setImageUrl(lastTweet.user.profileImageUrl);
                        Log.d("RepCollection", "Start picasso load");
                        Picasso.with(source).load(thisRep.imageUrl).resize(96, 96).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                thisRep.setBitmap(bitmap);
                                // FINAL ASYNC WATERFALL STAGE
                                checkFinished(source, idx, length);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                checkFinished(source, idx, length);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        checkFinished(source, idx, length);
                    }
                });

            }

            @Override
            public void failure(TwitterException e) {
                checkFinished(source, idx, length);
            }
        });
    }

    private void checkFinished(LocationActivity source, int index, int length) {
        received++;
        source.updateImage(index);
        Log.d("RepCollection", received + " out of " + length + " asyncs finished");
        if (received == length && length >= 3) {
            source.transitionWithReps();
        } else if (length <= 2) {
            criticalFailure(source);
        }
    }

    private void criticalFailure(LocationActivity source) {
        Toast.makeText(source, "Failed to load a representative, please try again", Toast.LENGTH_SHORT).show();
        received = -9999;
    }

}
