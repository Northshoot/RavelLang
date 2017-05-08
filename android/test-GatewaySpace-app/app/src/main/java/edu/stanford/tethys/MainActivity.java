package edu.stanford.tethys;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.stanford.ravel.generated.Gateway;
import org.stanford.ravel.rrt.android.DeleteMeKeys;
import org.stanford.ravel.rrt.tiers.BleEndpoint;
import org.stanford.ravel.rrt.tiers.Endpoint;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by gcampagn on 2/6/17.
 */
public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();
    //Application has to create endpoints
    //FIXME: this should me more automated
    //for now output is from Ravel compiler
    Gateway gatewaySpace;
    boolean mServiceBound = false;
    BleEndpoint localEndpoint = new BleEndpoint(3);
    Endpoint cloud;

    ArrayList<Endpoint> eps = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        enable_sdk_23();
        // start the service if not already
        Intent intent = new Intent(this, Gateway.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try { //"tcp://192.168.2.117:1234"
            cloud = Endpoint.fromString(1, new URI("tcp://192.168.2.117:1234"), Collections.<String, String>emptyMap());
        } catch(URISyntaxException |MalformedURLException e) {
            throw new RuntimeException(e);
        }
        setContentView(R.layout.main_activity);





    }

    @Override
    public void onDestroy(){
        mServiceBound = false;
        if(gatewaySpace != null){

            gatewaySpace.onDestroy();
        }
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        super.onDestroy();
    }
    protected ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Gateway.GatewayBinder myBinder = (Gateway.GatewayBinder) service;
            gatewaySpace = myBinder.getService();
            mServiceBound = true;
            localEndpoint.setLocal(true);
            eps.add(localEndpoint);
            //Can not add more end point than application is using
            //FIXME: commenting out cloud for two way test
            eps.add(cloud);
            gatewaySpace.setEndpoints(eps);
            InputStream stream = new ByteArrayInputStream(DeleteMeKeys.key_0.getBytes(UTF_8));
            gatewaySpace.setKey(stream);
            stream = new ByteArrayInputStream(DeleteMeKeys.key_1.getBytes(UTF_8));
            gatewaySpace.setKey(stream);
            stream = new ByteArrayInputStream(DeleteMeKeys.key_2.getBytes(UTF_8));
            gatewaySpace.setKey(stream);
            stream = new ByteArrayInputStream(DeleteMeKeys.key_3.getBytes(UTF_8));
            gatewaySpace.setKey(stream);


        }
    };
    public void enable_sdk_23(){
        //work around for SDK 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "Found SDK 23, requesting permission");
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {

                    ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }

        }
    }
}