package com.example.colby.represent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by colby on 2/29/16.
 */
public class PhoneToWatchService extends Service {
    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                mApiClient.connect();
                // dumb serializing
                JSONArray repObjects = new JSONArray();
                for (int i = 0; i < RepCollection.getInstance().size(); i++) {
                    try {
                        JSONObject repObject = new JSONObject();
                        Rep rep = RepCollection.getInstance().get(i);
                        repObject.put("name", rep.getDisplayName());
                        repObject.put("party", rep.party);
                        repObjects.put(repObject);

                        if (rep.bitmap != null) {
                            Asset asset = createAssetFromBitmap(rep.bitmap);
                            PutDataMapRequest dataMap = PutDataMapRequest.create("/image/" + i);
                            dataMap.getDataMap().putLong("time", new Date().getTime());
                            dataMap.getDataMap().putAsset("profileImage", asset);
                            PutDataRequest request = dataMap.asPutDataRequest();
                            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                                    .putDataItem(mApiClient, request);
                            Log.d("PhoneToWatch", "Sent bitmap asset");
                        }
                    } catch (JSONException e) {
                        continue;
                    }
                }
                JSONObject root = new JSONObject();
                double rvotes = RepCollection.getInstance().rVotes;
                double dvotes = RepCollection.getInstance().dVotes;

                try {
                    root.put("reps", repObjects);
                    root.put("rvotes", RepCollection.getInstance().rVotes);
                    root.put("dvotes", RepCollection.getInstance().dVotes);
                    root.put("county", RepCollection.getInstance().county);
                    root.put("state", RepCollection.getInstance().state);
                    sendMessage("/reload", root.toString());
                } catch (JSONException e) {
                }

            }
        }).start();

        return START_STICKY;
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }
}
