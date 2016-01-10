package com.example.celder.dancewatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.common.api.ResultCallback;

import java.util.concurrent.TimeUnit;

/**
 * Created by celder on 1/9/16.
 */
public class WatchActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private boolean mResolvingError=false;
    private static final String TAG = "WatchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launch);
        setContentView(R.layout.activity_watch);

        //Connect the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //UI elements with a simple CircleImageView
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // mimic waiting for a dance move to complete
//                try {
//                    TimeUnit.SECONDS.sleep(3);
//                } catch (InterruptedException ex) {
//                    Log.e(TAG, "InterruptedException during sleep: " + ex);
//                }
//                sendMessage();

//                CircledImageView mCircledImageView = (CircledImageView) stub.findViewById(R.id.circle);
//
//                //Listener to send the message (it is just an example)
//                mCircledImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sendMessage();
//                    }
//                });
            }
        });
    }


    /**
     * Send message to mobile handheld
     */
    private void sendMessage() {

        if (mNode != null && mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), HELLO_WORLD_WEAR_PATH, null).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            } else {
                                Log.d(TAG, "sent message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        } else {
            Log.e(TAG, "unable to send message");
            if (mNode == null) {
                Log.d(TAG, "mNode is null");
            }
            if (mGoogleApiClient == null) {
                Log.d(TAG, "mGoogleApiClient is null");
            }
            if (!mGoogleApiClient.isConnected()) {
                Log.d(TAG, "mGoogleApiClient is not connected");
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
        if (!mResolvingError) {
            Log.d(TAG, "attempting to connect to mGoogleApiClient");
            mGoogleApiClient.connect();
        }
    }

    /*
     * Resolve the node = the connected device to send the message to
     */
    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    Log.d(TAG, "connected node detected with id: " + node.getId() + ", toString: " + node.toString());
                    if (node.isNearby()) {
                        Log.d(TAG, "nearby node detected with id: " + node.getId() + ", toString: " + node.toString());
                        mNode = node;

                        // TODO: call this only after a song is detected
                        sendMessage();
                    }
                }
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Improve your code
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Improve your code
    }
}