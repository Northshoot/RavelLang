package org.stanford.ravel.rrt.android.user;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.stanford.ravel.rrt.android.system.RavelDefines;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener{

    public static final String TAG = "Blink";
//    private BleService mService = null;
//    private BluetoothDevice mDevice = null;
//    private BluetoothAdapter mBtAdapter = null;

    private Switch togle_switch = null;
    private Button btnConnectDisconnect;
    private int mState = RavelDefines.DEVICE_DISCONNECTED;




    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Intent mRavelServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBtAdapter == null) {
//            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
        togle_switch = (Switch) findViewById(R.id.togle_switch);
        togle_switch.setOnCheckedChangeListener(this);
        btnConnectDisconnect=(Button) findViewById(R.id.btn_select);
        service_init();


//        Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
//        startActivityForResult(newIntent, RavelDefines.REQUEST_SELECT_DEVICE);






//        LocalBroadcastManager.getInstance(this).registerReceiver(gcmBroadcastReceiver, makeGCMIntentFilter());
//        ledStatusModel.setField_iot_device(ID);
        //work around for SDK 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "Found SDK 23, requesting permission");
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            RavelDefines.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                }
            }
        }


    }



//TODO: we cannot connect to the service because it its a process. The only way to do it is via AIDL
//TODO:  it has to be a process to run independently after the gui exits
//TODO: details http://stackoverflow.com/questions/1916253/bind-service-to-activity-in-android
//    public ServiceConnection mRavelControllerConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
//             mRavelController = ((AndroidRRT.LocalBinder) rawBinder).getService();
//            Log.d(TAG, "onServiceConnected mRavelController= " + mRavelController);
////            if (!mRavelController.initialize()) {
////                Log.e(TAG, "Unable to initialize Bluetooth");
////                finish();
////            }
//        }
//
//        public void onServiceDisconnected(ComponentName classname) {
//            mRavelController = null;
//        }
//    };






    private void service_init() {
        mRavelServiceIntent = new Intent(getApplicationContext(), RavelController.class);
        startService(mRavelServiceIntent);
        registerReceiver();

    }

    private BroadcastReceiver mRaveConrollerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            if( intent.getAction().equals(BleDefines.ACTION_GATT_CONNECTED)){
                ((TextView)findViewById(R.id.deviceName)).setText(intent.getStringExtra("DEVICE_ADDRESS"));
            } else if (intent.getAction().equals(RavelController.BROADCAST_ACTION)){
                setToggle();
            }



        }
    };

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(RavelController.BROADCAST_ACTION);
        filter.addAction(BleDefines.ACTION_GATT_CONNECTED);
        registerReceiver(mRaveConrollerReceiver, filter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

//        unbindService(mRavelControllerConnection);
//        mRavelControllerConnection= null;

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//
//            case RavelDefines.REQUEST_SELECT_DEVICE:
//                //When the DeviceListActivity return, with the selected device address
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
//                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
//
//                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
//                    ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - connecting");
//                    mService.connect(deviceAddress);
//
//
//                }
//                break;
//            case RavelDefines.REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    // User did not enable Bluetooth or an error occurred
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//            default:
//                Log.e(TAG, "wrong request code");
//                break;
//        }
    }



    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (mState == RavelDefines.DEVICE_CONNECTED) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("Ravel BLE service running in background.\n             Disconnect to exit");
        }
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quite Ravel GUI?")
                    .setMessage(R.string.popup_message)
                    .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.popup_no, null)
                    .show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
