package org.stanford.ravel.rrt.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import org.stanford.ravel.rrt.android.utils.ByteWork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lauril on 3/8/17.
 */

public class BlePacket implements Serializable {

    private String address;
    private byte[] data;
    private int flags = 0;
    private int indx=0;
    private int length=0;
    private boolean partial = false;
    private boolean last = false;
    public static final int FLAG_PARTIAL = 1;
    public static final int FLAG_LAST = 2;

    public BlePacket(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        this.data = characteristic.getValue();
        this.address = gatt.getDevice().getAddress();
    }

    public BlePacket(byte[] data, String device_address){
        this.indx = ByteWork.convertTwoUnsignedBytesToInt(ByteWork.getBytes(data, 0, 2));
        this.length = ByteWork.convertTwoUnsignedBytesToInt(ByteWork.getBytes(data, 2, 4));
        this.flags = ByteWork.convertTwoUnsignedBytesToInt(ByteWork.getBytes(data, 4, 6));
        this.partial = (this.flags & FLAG_PARTIAL) == FLAG_PARTIAL;
        this.last = (this.flags & FLAG_LAST) == FLAG_LAST;
        this.data = ByteWork.getBytes(data, 6, 6+this.length);
        this.address = device_address;
    }

    public BlePacket(byte[] data){
        this.data = data;
    }

    public void setData(byte[] d) {this.data = d ;}
    public byte[] getData(){ return data; }
    public String getAddress(){ return address; }

    public boolean isLast() {return this.last ; }

    public void setAddress(String a){ address = a; }

    public static byte[] fromArray(ArrayList<BlePacket> m_frag) {
        byte[] result = m_frag.get(0).getData();
        for(int i = 1 ; i < m_frag.size(); i++){
            byte [] a = m_frag.get(i).getData();
            System.arraycopy(a, 0, result, 0, a.length);
        }
        return result;
    }


}
