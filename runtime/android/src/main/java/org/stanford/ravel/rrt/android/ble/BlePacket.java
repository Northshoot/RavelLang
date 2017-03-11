package org.stanford.ravel.rrt.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import java.io.Serializable;

/**
 * Created by lauril on 3/8/17.
 */

public class BlePacket implements Serializable {

    private String address;
    private byte[] data;


    public BlePacket(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        this.data = characteristic.getValue();
        this.address = gatt.getDevice().getAddress();
    }

    public BlePacket(byte[] data, String device_address){
        this.data = data;
        this.address = device_address;
    }

    public BlePacket(byte[] data){
        this.data = data;
    }

    public void setData(byte[] d) {this.data = d ;}
    public byte[] getData(){ return data; }
    public String getAddress(){ return address; }
    public void setAddress(String a){ address = a; }
}
