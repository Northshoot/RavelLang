package edu.stanford.ledcontrol;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import edu.stanford.ledcontrol.model.LedStatusRepresentation;
import edu.stanford.ledcontrol.presenter.LedStatusPresenter;
import edu.stanford.ravel.controller.RavelController;
import edu.stanford.ravel.defines.BleDefines;
import edu.stanford.ravel.defines.RavelDefines;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener{

    public static final String TAG = "Blink";
//    private BleService mService = null;
//    private BluetoothDevice mDevice = null;
//    private BluetoothAdapter mBtAdapter = null;

    private Switch togle_switch = null;
    private Button btnConnectDisconnect;
    private int mState = RavelDefines.DEVICE_DISCONNECTED;


    private LedStatusPresenter ledPresenter;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private LedStatusPresenter ledStatusPresenter;
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
        ledStatusPresenter = new LedStatusPresenter(this);
        service_init();


//        Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
//        startActivityForResult(newIntent, RavelDefines.REQUEST_SELECT_DEVICE);


        // Handle Disconnect & Connect button
//        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mBtAdapter.isEnabled()) {
//                    Log.i(TAG, "onClick - BT not enabled yet");
//                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableIntent, RavelDefines.REQUEST_ENABLE_BT);
//                }
//                else {
//                	if (btnConnectDisconnect.getText().equals("Connect")){
//
//                		//Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices
//
//            			Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
//            			startActivityForResult(newIntent, RavelDefines.REQUEST_SELECT_DEVICE);
//        			} else {
//        				//Disconnect button pressed
//        				if (mDevice!=null)
//        				{
//        					mService.disconnect();
//
//        				}
//        			}
//                }
//            }
//        });




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

    private boolean expectToggleCall = false;

    private void setToggle(){
        LedStatusRepresentation ls = ledStatusPresenter.getLastLedStatus();
        if(ls != null) {
            expectToggleCall = true;
            ((TextView) findViewById(R.id.field_origin_value)).setText(String.valueOf(RavelDefines.EMBEDDED));
            updateGUI(ls);
            if (ls.led_status == 0) {
                togle_switch.setChecked(false);
            } else {
                togle_switch.setChecked(true);
            }
        }
    }

    private void updateGUI(LedStatusRepresentation ls){
        ((TextView) findViewById(R.id.update_time)).setText(ls.time);
        ((TextView) findViewById(R.id.field_status_value)).setText("  " + String.valueOf(ls.led_status));

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //TODO: notify the presenter
        if(!expectToggleCall) {
            Log.d(TAG, "onCheckedChanged " + String.valueOf(isChecked));
            int data;
            ((TextView) findViewById(R.id.field_origin_value)).setText("  " + String.valueOf(RavelDefines.GATEWAY));
            if (isChecked) {
                data = 1;
            } else {
                data = 0;
            }
            LedStatusRepresentation ls = new LedStatusRepresentation(data, null, RavelDefines.GATEWAY);
            ledStatusPresenter.insertLedStatus(ls);
            updateGUI(ls);
        } else {
            expectToggleCall = false;
        }
    }

//TODO: we cannot connect to the service because it its a process. The only way to do it is via AIDL
//TODO:  it has to be a process to run independently after the gui exits
//TODO: details http://stackoverflow.com/questions/1916253/bind-service-to-activity-in-android
//    public ServiceConnection mRavelControllerConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
//             mRavelController = ((RavelController.LocalBinder) rawBinder).getService();
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
