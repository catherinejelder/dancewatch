package com.example.celder.dancewatch;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.example.celder.data.SimpleDanceRecord;
import com.example.celder.data.Song;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.common.api.ResultCallback;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by celder on 1/9/16.
 */
public class WatchActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SensorEventListener {

    Node mNode; // the connected device to send the message to
    GoogleApiClient mGoogleApiClient;
    private static final String HELLO_WORLD_WEAR_PATH = "/hello-world-wear";
    private boolean mResolvingError = false;
    private static final String TAG = "WatchActivity";

    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private final float[] mRotationMatrix = new float[16];
    private float deviceOrientation[] = new float[3];

    private SimpleDanceRecord danceRecord;
    private Handler messageHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        //Connect the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // find and initialize rotation vector sensor
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // initialize rotation matrix
        mRotationMatrix[0] = 1;
        mRotationMatrix[4] = 1;
        mRotationMatrix[8] = 1;
        mRotationMatrix[12] = 1;

        //UI elements with a simple CircleImageView
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            }
        });

        danceRecord = new SimpleDanceRecord();

        // periodically log orientation data
        // TODO: is 40 ms a good interval?
        Runnable orientationLogger = new Runnable() {
            @Override
            public void run() {
                // update dance record
                danceRecord.addPoint(deviceOrientation);
//                Log.d(TAG, "orientation before translation: " + Arrays.toString(deviceOrientation));
                Log.d(TAG, "logging orientation: [" + danceRecord.zs.peekLast() + ", " + danceRecord.xs.peekLast() + ", " + danceRecord.ys.peekLast() + "]");
                messageHandler.postDelayed(this, 40);
            }
        };

        messageHandler.postDelayed(orientationLogger, 40);

        // periodically check if a song has been detected
        Runnable songChecker = new Runnable() {
            @Override
            public void run() {
                Song song = danceRecord.isSong();
                Log.d(TAG, "song so far: " + song);
//                Log.d(TAG, "stats so far, for danceRecord of size: (" + danceRecord.zs.size() + ", " + danceRecord.xs.size() + ", " + danceRecord.ys.size() + ")\n"
//                        + "zs: " + danceRecord.zs
//                        + "\n xs: " + danceRecord.xs
//                        + "\n ys: " + danceRecord.ys
//                        + "\n" + danceRecord.getStats());
                Log.d(TAG, "stats so far: \n" + danceRecord.getStats());
                if (song != Song.NONE) {
                    sendMessage(song.uri);
                }
                messageHandler.postDelayed(this, 250);
            }
        };

        messageHandler.postDelayed(songChecker, 250);
    }

    /**
     * Send message to mobile handheld
     */
    private void sendMessage(String uri) {
        Log.d(TAG, "sendMessage called");
        if (mNode != null && mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
//            byte[] songUri = "spotify:track:5R9a4t5t5O0IsznsrKPVro".getBytes(StandardCharsets.UTF_8);
            Log.d(TAG, "sendMessage sending song with name: " + uri);

            byte[] songUri = uri.getBytes(StandardCharsets.UTF_8);

            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), HELLO_WORLD_WEAR_PATH, songUri).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            } else {
                                Log.d(TAG, "sent message to mobile successfully");
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
//        if (!mResolvingError) {
//            Log.d(TAG, "attempting to connect to mGoogleApiClient");
//            mGoogleApiClient.connect();
//        }
    }

    @Override
    protected void onResume() {
        Log.d("WatchActivity", "onResume called");
        super.onResume();
//        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
//        mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if (!mResolvingError) { // TODO: get rid of this
            Log.d(TAG, "attempting to connect to mGoogleApiClient");
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        Log.d("WatchActivity", "onPause called");
        super.onPause();
        mSensorManager.unregisterListener(this);
        mGoogleApiClient.disconnect();
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
//                        sendMessage();
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

    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.d(TAG, "onSensorChanged called");
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            float orientation[] = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
//            Log.d(TAG, "getOrientation = " + Arrays.toString(orientation));
            // update orientation field
            this.deviceOrientation[0] = orientation[0];
            this.deviceOrientation[1] = orientation[1];
            this.deviceOrientation[2] = orientation[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged called");
    }
}