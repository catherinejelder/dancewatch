package com.example.celder.dancewatch;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by celder on 1/9/16.
 */
public class ListenerServiceFromWear extends WearableListenerService {

    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private static final String TAG = "ListenerServiceFromWear";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        Log.d(TAG, "message received, event: " + messageEvent);

        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {
            Log.d(TAG, "attempting to start MainActivity");

            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

    }

}