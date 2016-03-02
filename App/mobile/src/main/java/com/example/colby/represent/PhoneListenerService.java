package com.example.colby.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by colby on 2/29/16.
 */
public class PhoneListenerService extends WearableListenerService {
    private static final String repPath = "/rep";
    private static final String randomPath = "/random";

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
            intent.putExtra("rep", RepsActivity.reps.get(idx));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(randomPath)) {
            Intent intent = new Intent(this, RepsActivity.class);
            // Should actually be random, but for demo
            intent.putExtra("zipcode", 40000);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
