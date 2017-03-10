package org.stanford.ravel.rrt.android.ble;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.stanford.ravel.rrt.android.system.RavelDefines;
import org.stanford.ravel.rrt.android.system.RavelErrorCodes;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * Created by lauril on 1/21/16.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RavelBleService extends Service {

    private final static String TAG = RavelBleService.class.getSimpleName();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    List<BluetoothGattService> mBluetoohServiceList;

    private ScanSettings settings;
    List<ScanFilter> filters;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler;
    List<BluetoothDevice> deviceList;
    private boolean mScanning;
    private String mBluetoothDeviceAddress;

    private int mBLEConnectionState = BLE_STATE_DISCONNECTED;
    private static final int BLE_STATE_DISCONNECTED = 0;
    private static final int BLE_STATE_CONNECTING = 1;
    private static final int BLE_STATE_CONNECTED = 2;
    private static final int BLE_STATE_INITIALIZED = 3;

    public boolean EMBEDDED_CONNECTED = false; // indicates if EMBEDDED device connected


    private Handler.Callback embedded_callback=null;


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /**
         * Callback indicating when GATT client has connected/disconnected to/from a remote
         * GATT server.
         *
         * @param gatt     GATT client
         * @param status   Status of the connect or disconnect operation.
         *                 {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         * @param newState Returns the new connection state. Can be one of
         *                 {@link BluetoothProfile#STATE_DISCONNECTED} or
         *                 {@link BluetoothProfile#STATE_CONNECTED}
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //TODO: fix multiple device handling
                mBLEConnectionState = BLE_STATE_CONNECTED;
                EMBEDDED_CONNECTED = true;
                //broadcast connection to the driver
                //Driver creates new endpoint
                broadcastUpdate(BleDefines.ACTION_GATT_CONNECTED, gatt.getDevice().getAddress());

                Log.i(TAG, "Connected to GATT server.");
                //Scan for services
                if ( gatt.discoverServices() ) {
                    Log.i(TAG, "Service Discovery started.");
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //TODO: fix multiple device handling
                mBLEConnectionState = BLE_STATE_DISCONNECTED;
                EMBEDDED_CONNECTED = false;
                broadcastUpdate(BleDefines.ACTION_GATT_DISCONNECTED, gatt.getDevice().getAddress());
                //start scanning after predefined period
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanLeDevice();
                    }
                }, BleDefines.BLE_RECONNECT_TIME);
                Log.i(TAG, "Device disconnected.");
            } else {
                Log.e(TAG, "Should not be in this state: " + String.valueOf(status) +
                        " for the gatt " + gatt.toString());
            }
        }

        /**
         * Callback invoked when the list of remote services, characteristics and descriptors
         * for the remote device have been updated, ie new services have been discovered.
         *
         * @param gatt   GATT client invoked {@link BluetoothGatt#discoverServices}
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the remote device
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //broadcastUpdate(BleDefines.ACTION_GATT_SERVICES_DISCOVERED);
                if (mBluetoothGatt != null) {
                    mBluetoohServiceList = mBluetoothGatt.getServices();
                    /** analyze services,
                     */
                    checkForServices(mBluetoohServiceList);
                } else {
                    Log.e(TAG, " GATT null when retrieving services");
                }

            } else {
                Log.w(TAG, "onServicesDiscovered error status: " + status);
            }
        }

        /**
         * Callback reporting the result of a characteristic read operation.
         *
         * @param gatt           GATT client invoked {@link BluetoothGatt#readCharacteristic}
         * @param characteristic Characteristic that was read from the associated
         *                       remote device.
         * @param status         {@link BluetoothGatt#GATT_SUCCESS} if the read operation
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //TODO: dispatch characteristic to Driver

                Log.e(TAG, "onCharacteristicRead error status: " + status);
            } else {
                Log.e(TAG, "onCharacteristicRead error status: " + status);
            }
        }

        /**
         * Callback indicating the result of a characteristic write operation.
         * <p/>
         * <p>If this callback is invoked while a reliable write transaction is
         * in progress, the value of the characteristic represents the value
         * reported by the remote device. An application should compare this
         * value to the desired value to be written. If the values don't match,
         * the application must abort the reliable write transaction.
         *
         * @param gatt           GATT client invoked {@link BluetoothGatt#writeCharacteristic}
         * @param characteristic Characteristic that was written to the associated
         *                       remote device.
         * @param status         The result of the write operation
         *                       {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //TODO: notify send_done
            Message message = new Message();
            message.setAsynchronous(true);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //report back to the model the characteristic
                message.arg1 = RavelDefines.SEND_DONE_SUCCESS;
                sendCallback(embedded_callback, message);

            } else {
                message.arg1 = RavelDefines.SEND_DONE_ERROR;
                sendCallback(embedded_callback, message);
                Log.w(TAG, "onCharacteristicWrite error status: " + status);
            }
            embedded_callback = null;
        }

        /**
         * Callback triggered as a result of a remote characteristic notification.
         *
         * @param gatt           GATT client the characteristic is associated with
         * @param characteristic Characteristic that has been updated as a result
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //read data
            Log.d(TAG, "onCharacteristicChanged: " + characteristic.getUuid());
            BlePacket ble_pkt = new BlePacket( gatt, characteristic);



        }

        /**
         * Callback reporting the result of a descriptor read operation.
         *
         * @param gatt       GATT client invoked {@link BluetoothGatt#readDescriptor}
         * @param descriptor Descriptor that was read from the associated
         *                   remote device.
         * @param status     {@link BluetoothGatt#GATT_SUCCESS} if the read operation
         */
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            } else {
                Log.w(TAG, "onDescriptorRead error status: " + status);
            }
        }

        /**
         * Callback indicating the result of a descriptor write operation.
         *
         * @param gatt       GATT client invoked {@link BluetoothGatt#writeDescriptor}
         * @param descriptor Descriptor that was writte to the associated
         *                   remote device.
         * @param status     The result of the write operation
         *                   {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.w(TAG, "onDescriptorWrite error status: " + status);
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d(TAG, )
//            } else {
//
//            }
        }

        /**
         * Callback invoked when a reliable write transaction has been completed.
         *
         * @param gatt   GATT client invoked {@link BluetoothGatt#executeReliableWrite}
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the reliable write
         */
        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            } else {
                Log.w(TAG, "onReliableWriteCompleted error status: " + status);
            }
        }

        /**
         * Callback reporting the RSSI for a remote device connection.
         * <p/>
         * This callback is triggered in response to the
         * {@link BluetoothGatt#readRemoteRssi} function.
         *
         * @param gatt   GATT client invoked {@link BluetoothGatt#readRemoteRssi}
         * @param rssi   The RSSI value for the remote device
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the RSSI was read successfully
         */
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            } else {
                Log.w(TAG, "onReadRemoteRssi error status: " + status);
            }
        }

        /**
         * Callback indicating the MTU for a given device connection has changed.
         * <p/>
         * This callback is triggered in response to the
         * {@link BluetoothGatt#requestMtu} function, or in response to a connection
         * event.
         *
         * @param gatt   GATT client invoked {@link BluetoothGatt#requestMtu}
         * @param mtu    The new MTU size
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the MTU has been changed successfully
         */
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

            } else {
                Log.w(TAG, "onMtuChanged error status: " + status);
            }
        }
    };


    private void checkForServices(List<BluetoothGattService> mBluetoohServiceList) {
        Log.d(TAG, "Checking BLE services");
        String error = null;
        try {
            //get all models by service, enable all notify characteristics
            //We only check for ravel models
            //TODO: multiple ravel models
            for (BluetoothGattService bleS : mBluetoohServiceList) {
                if(bleS.getUuid() == RavelGattAtrributes.RAVEL_DATA_MODEL_UUID) {
                    enableNotification(RavelGattAtrributes.RAVEL_DATA_MODEL_READ_CHAR_UUID, bleS.getUuid());
                } else {
                    Log.e(TAG, "no compatible service was found");
                }
            }

        } catch (Exception e) {
            error = e.getMessage();
        } finally {
            if(error != null) {
                Log.e(TAG, error);
            }
        }
    }


    public void enableNotification(UUID notification, UUID service) {
        BluetoothGattCharacteristic charnot = mBluetoothGatt.getService(service).getCharacteristic(notification);
        if (charnot != null) enableNotification(charnot);
    }

    private void enableNotification(BluetoothGattCharacteristic characteristic){
        mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        //TODO: not sure this is the right way
        //https://devzone.nordicsemi.com/question/55669/enabling-multiple-notifications-characteristic/
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(BleDefines.CLIENT_CHARACTERISTIC_CONFIG);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }


    @Override
    public void onCreate() {
        /**
         * Create and show notification in icon bar that Ravel is running
         */
        mHandler = new Handler(Looper.getMainLooper());

        /**
         *  bluetooth
         */
        //create BLE manager
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e(TAG, "Can not start BLE service");
            this.onDestroy();
        }
        deviceList = new ArrayList<>();
        if ( initialize() ) {
            mBLEConnectionState = BLE_STATE_INITIALIZED;
            scanLeDevice();
        }

        /** END BLE */

        /**
         * handle GCM
         */
        //start GCM service
        // we can not extend two classes

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mBLEConnectionState = BLE_STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mBLEConnectionState = BLE_STATE_CONNECTING;
        return true;
    }


    private void scanLeDevice() {
        bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setDeviceName(BleDefines.SPACE_NAME).build());
        mScanning = true;
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                bluetoothLeScanner.stopScan(mLeScanCallback);
                scanningDone();

            }
        }, BleDefines.SCAN_PERIOD);

        mScanning = true;
        bluetoothLeScanner.startScan(filters, settings, mLeScanCallback);
    }

    private ScanCallback mLeScanCallback =
            new ScanCallback() {

                public void onScanResult(int callbackType, final ScanResult result){
                    Log.d(TAG, "onScanResult " + result.getDevice() + "" + result.getRssi());
                    addDevice(result.getDevice(), result.getRssi());
                }


                public void onScanFailed(int errorCode){
                    Log.e(TAG, "onScanFailed " + errorCode);

                }
                public void onBatchScanResults(List<ScanResult> results){
                    Log.e(TAG,"onBatchScanResults " +  "onBatchScanResults");

                }


            };

    private void scanningDone(){
        Log.i(TAG, "Scanning done, found: " + deviceList.size() + " device (s)");
        if(deviceList.size() > 0) {
            for (BluetoothDevice dev :deviceList) {
                //TODO: create an endpoint

                //connect to the device
                connect(dev.getAddress());
            }
            BluetoothDevice bluetoothDevice = deviceList.get(0);
        } else {
            //we need to start an activity for intent and grab the address of the device
            //TODO: should not get here!
            Log.e(TAG, "Empty device list");

        }

    }

    private void addDevice(BluetoothDevice device, int rssi) {
        boolean deviceFound = false;

        for (BluetoothDevice listDev : deviceList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }

        if (!deviceFound) {
            deviceList.add(device);
        }
    }
    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null && mBLEConnectionState == BLE_STATE_DISCONNECTED) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        } else {
            Log.e(TAG, "Trying to initialize manager in state: " + String.valueOf(mBLEConnectionState)
                    + " with manager " + mBluetoothManager.toString());
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }



    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * This is called if the service is currently running and the user has
     * removed a task that comes from the service's application.  If you have
     * set {@link ServiceInfo#FLAG_STOP_WITH_TASK ServiceInfo.FLAG_STOP_WITH_TASK}
     * then you will not receive this callback; instead, the service will simply
     * be stopped.
     *
     * @param rootIntent The original root Intent that was used to launch
     *                   the task that is being removed.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved");
    }

    /**
     * Print the Service's state into the given stream.  This gets invoked if
     * you run "adb shell dumpsys activity service &lt;yourservicename&gt;"
     * (note that for this command to work, the service must be running, and
     * you must specify a fully-qualified service name).
     * This is distinct from "dumpsys &lt;servicename&gt;", which only works for
     * named system services and which invokes the {@link IBinder#dump} method
     * on the {@link IBinder} interface registered with ServiceManager.
     *
     * @param fd     The raw file descriptor that the dump is being sent to.
     * @param writer The PrintWriter to which you should dump your state.  This will be
     *               closed for you after you return.
     * @param args   additional arguments to the dump request.
     */
    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.d(TAG, "dump");
    }

    private void broadcastUpdate(final int action, final String extra){
        Log.d(TAG,"Sending broadcast: " + action +  " extra " + extra);
        final Intent intent = new Intent(BleDefines.INTENT_BLE_FILTER);
        intent.putExtra(BleDefines.COMMAND, action);
        intent.putExtra(BleDefines.EXTRA_DATA, extra);
        sendBroadcast(intent);
    }


    /**
     * Generic method that writes to the model instance on the embedded device
     * BLE assumption here!
     * TODO: extract BLE to a generic method

     */

    public boolean write_to_embedded(Handler.Callback callback, BlePacket pkt) {
        //TODO: write to the characteristic
        this.embedded_callback = callback;
        if( mBluetoothGatt != null) {
            //TODO: assuming here single device
            BluetoothGattService service = mBluetoothGatt.getService(RavelGattAtrributes.RAVEL_DATA_MODEL_UUID);
            if (service == null){
                //Should not end up here
                Log.e(TAG, RavelErrorCodes.NO_SUCH_SERVICE);
                return false;
            }
            BluetoothGattCharacteristic writeChar = service.getCharacteristic(RavelGattAtrributes.RAVEL_DATA_MODEL_WRITE_CHAR_UUID);
            if (service == null) {
                //Should not end up here
                Log.e(TAG, RavelErrorCodes.NO_SUCH_WRITE_CHARACTERISTIC);
                return false;
            }
            writeChar.setValue(pkt.getData());
            boolean status = mBluetoothGatt.writeCharacteristic(writeChar);
            if (!status) {
                Log.e(TAG, RavelErrorCodes.WRITE_CHARACTERISTIC_ERROR);
                return false;
            }
            Log.d(TAG,"writeCharacteristic Success");
        } else {
            Log.d(TAG,"BLE Disconnected");
        }
        return true;
    }


    private void sendCallback(Handler.Callback callback, Message message){
        Handler handler = new Handler(callback);
        handler.sendMessage(message);
    }



}
