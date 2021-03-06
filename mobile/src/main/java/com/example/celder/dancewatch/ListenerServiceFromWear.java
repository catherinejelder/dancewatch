package com.example.celder.dancewatch;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

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
        Log.d(TAG, "messageEvent.getPath: " + messageEvent.getPath());

        if (messageEvent.getPath().equals(HELLO_WORLD_WEAR_PATH)) {
            String songUri = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, "got a message with songUri: " + songUri);
            Log.d(TAG, "attempting to restart MainActivity");

            Intent startIntent = new Intent(this, MainActivity.class);
            // if a song is already playing, just log the message
            // if no song is playing, deliver the message and start playing the new song
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // if the activity is already running at the top of the history stack, don't relaunch it
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startIntent.putExtra("SPOTIFY_URI", songUri);
            startActivity(startIntent);
        }

    }

}