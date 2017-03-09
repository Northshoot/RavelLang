package org.stanford.ravel.rrt.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.android.ble.BleDefines;
import org.stanford.ravel.rrt.android.ble.RavelBleService;
import org.stanford.ravel.rrt.tiers.BleEndpoint;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.tiers.JavaDriver;
import org.stanford.ravel.rrt.tiers.JavaDurableStorage;
import org.stanford.ravel.rrt.tiers.RavelIOException;
import org.stanford.ravel.rrt.tiers.RavelSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidDriver extends JavaDriver {

    private final static String TAG = AndroidDriver.class.getSimpleName();

    private final Context ctx;
    private final Map<BleEndpoint, RavelSocket> socketClients = new HashMap<>();
    // BLE related variables
    private boolean m_connected_ble = false;
    private boolean m_notifications_enabled = false;
    private boolean m_ble_endpoint_started = false;

    //Connection states
    public boolean EMBEDDED_CONNECTED = false; // indicates if EMBEDDED device connected
    public boolean GATEWAY_CONNECTED = false; // indicates open gateway application
    public boolean CLOUD_CONNECTED = false; // indicates if cloud is connected

    private Intent mRavelServiceIntent;

    private BroadcastReceiver mRaveConrollerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){

                case BleDefines.ACTION_GATT_CONNECTED:
                    Log.d(TAG, "onReceive: ACTION_GATT_CONNECTED");
                    break;
                case BleDefines.ACTION_GATT_DISCONNECTED:
                    Log.d(TAG, "onReceive: ACTION_GATT_CONNECTED");
                    break;
                case BleDefines.ACTION_DATA_AVAILABLE:
                    Log.d(TAG, "onReceive: ACTION_DATA_AVAILABLE");
                    break;
                case BleDefines.ACTION_GATT_SERVICES_DISCOVERED:
                    Log.d(TAG, "onReceive: ACTION_GATT_SERVICES_DISCOVERED");
                    break;
                case BleDefines.DEVICE_DOES_NOT_SUPPORT_RAD:
                    Log.d(TAG, "onReceive: DEVICE_DOES_NOT_SUPPORT_RAD");
                    break;

                default:
                    Log.e(TAG, "Unknown type of intent " + intent.getAction());
            }




        }
    };
    public AndroidDriver(DispatcherAPI dispatcher, Context ctx) {
        super(dispatcher);
        this.ctx = ctx;
    }

    protected JavaDurableStorage createStorage() {
        return new JavaDurableStorage(ctx.getDir("storage", Context.MODE_PRIVATE));
    }

    @Override
    protected void sendDataThread(RavelPacket data, Endpoint endpoint) throws RavelIOException {
        switch (endpoint.getType()) {
            case BLE:
                // send to a ble device
                // TODO
                break;
            case GCM:
                // send to the cloud using GCM
                // TODO
                break;

            default:
                super.sendDataThread(data, endpoint);
        }
    }

    @Override
    protected Error startLocalEndpoint(Endpoint endpoint) {
        switch (endpoint.getType()) {
            case BLE:
                // open ble listening socket
                // TODO
                start_ble_service();
                break;
            case GCM:
                // prepare connection from GCM
                // TODO
                break;

            default:
                return super.startLocalEndpoint(endpoint);
        }

        return Error.SUCCESS;
    }

    @Override
    protected Error startRemoteEndpoint(Endpoint endpoint) {
        switch (endpoint.getType()) {
            case BLE:
                // prepare connection to ble device
                // TODO
                break;
            case GCM:
                // prepare connection to GCM
                // TODO
                break;

            default:
                return super.startRemoteEndpoint(endpoint);
        }

        return Error.SUCCESS;
    }

    void start_ble_service(){
        //init ble service
        mRavelServiceIntent = new Intent(ctx.getApplicationContext(), RavelBleService.class);
        //register receiver
        ctx.startService(mRavelServiceIntent);

    }
}
