package org.stanford.ravel.rrt.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by lauril on 3/8/17.
 */

public class BlePacket {

    private String device_address;
    private byte[] data;


    public BlePacket(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        this.data = characteristic.getValue();
        this.device_address = gatt.getDevice().getAddress();
    }

    public BlePacket(byte[] data, String device_address){
        this.data = data;
        this.device_address = device_address;
    }

    public byte[] getData(){ return data; }
    public String getAddress(){ return device_address; }
}
