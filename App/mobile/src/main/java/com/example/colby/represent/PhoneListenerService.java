package com.example.colby.represent;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by colby on 2/29/16.
 */
public class PhoneListenerService extends WearableListenerService {
    private static final String repPath = "/rep";
    private static final String randomPath = "/random";

    public static final double LAT_LOWER = 33.581178;
    final static double LAT_UPPER = 41.253084;
    public static final double LONG_LOWER = -119.957391;
    final static double LONG_UPPER = -81.812763;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("Phone", "Got path " + messageEvent.getPath());
        if (messageEvent.getPath().substring(0, 4).equalsIgnoreCase(repPath)) {
            int idx;
            try {
                idx = Integer.parseInt(messageEvent.getPath().substring(5,6));
            } catch (Exception e) {
                return;
            }
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("INDEX", idx);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(randomPath)) {
            Intent intent = new Intent(this, LocationActivity.class);
            // Should actually be random, but for demo
            double rlat = ThreadLocalRandom.current().nextDouble(LAT_LOWER, LAT_UPPER);
            double rlong = ThreadLocalRandom.current().nextDouble(LONG_LOWER, LONG_UPPER);

            intent.putExtra("lat", rlat);
            intent.putExtra("long", rlong);
            intent.putExtra("random", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
