package org.stanford.ravel.rrt.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.android.ble.BleDefines;
import org.stanford.ravel.rrt.android.ble.BlePacket;
import org.stanford.ravel.rrt.android.ble.RavelBleService;
import org.stanford.ravel.rrt.tiers.BleEndpoint;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.tiers.JavaDriver;
import org.stanford.ravel.rrt.tiers.JavaDurableStorage;
import org.stanford.ravel.rrt.tiers.RavelIOException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidDriver extends JavaDriver {

    private final static String TAG = AndroidDriver.class.getSimpleName();
    private final DispatcherAPI appDispatcher;
    private final Context ctx;
    private final Map<String, BleEndpoint> bleClients = new HashMap<>();
    // BLE related variables
    private boolean m_connected_ble = false;
    private boolean m_ble_endpoint_started = false;

    //Connection states
    public boolean EMBEDDED_CONNECTED = false; // indicates if EMBEDDED device connected
    public boolean GATEWAY_CONNECTED = false; // indicates open gateway application

    //FIXME
    //normaly the gateway will be the forwarding station and will need different endpoint
    //the endpoint should be generated based on the space and endpoint configuration
    //for now auto start
    /**
     * XXSpace:
     *  endpoint:
     *      type: BLE/ZIGBEE/TCP/RAWSOCKET/UDP/HTTP/HTTPS/WEBSOCKET
     *      address: default
     *      dns: xxxx
     *      register: xxx.xxx.xxx.xxx.
     */

    private Intent mRavelServiceIntent;

    private BroadcastReceiver mRaveBleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            String device_address ="";
            switch (data.getInt(BleDefines.COMMAND,0)) {

                case BleDefines.ACTION_GATT_CONNECTED:
                    Log.d(TAG, "onReceive: ACTION_GATT_CONNECTED");
                    device_address = intent.getStringExtra(BleDefines.EXTRA_DATA);
                    BleEndpoint ble_e = new BleEndpoint(device_address);
                    ble_e.connected();
                    bleClients.put(device_address, ble_e);
                    EMBEDDED_CONNECTED = true;
                    break;
                case BleDefines.ACTION_GATT_DISCONNECTED:
                    Log.d(TAG, "onReceive: ACTION_GATT_CONNECTED");
                    device_address = intent.getStringExtra(BleDefines.EXTRA_DATA);
                    bleClients.remove(device_address);
                    break;
                case BleDefines.ACTION_DATA_AVAILABLE:
                    Log.d(TAG, "onReceive: ACTION_DATA_AVAILABLE");
                    BlePacket pkt = (BlePacket) data.getSerializable(BleDefines.EXTRA_DATA);
                    BleEndpoint ble_p = bleClients.get(pkt.getAddress());
                    appDispatcher.driver__dataReceived(RavelPacket.fromNetwork(pkt.getData()), ble_p);
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


    public AndroidDriver(DispatcherAPI dispatcher,  Context ctx) {
        super(dispatcher, createStorage(ctx));
        this.appDispatcher = dispatcher;
        this.ctx = ctx;
        Log.d(TAG, "Starting the driver");
        start_ble_service();
    }

    private static JavaDurableStorage createStorage(Context ctx) {
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
        Log.d(TAG, "Starting BLE service");
        mRavelServiceIntent = new Intent(ctx.getApplicationContext(), RavelBleService.class);
        //register receiver
        ctx.startService(mRavelServiceIntent);
        ctx.registerReceiver((mRaveBleMessageReceiver), new IntentFilter(BleDefines.INTENT_BLE_FILTER));


    }
}
