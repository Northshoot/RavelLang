package org.stanford.ravel.rrt.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import org.stanford.ravel.rrt.android.utils.ByteWork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lauril on 3/8/17.
 */

public class BlePacket implements Serializable {

    private String address;
    private byte[] data;
    private int flags = 0;
    private int indx=0;
    private int length=0;
    private boolean last = false;
    public static final int FLAG_LAST = 1;
    private int protocol_offset = 3;

    public BlePacket(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        this(characteristic.getValue(), gatt.getDevice().getAddress());
    }

    public BlePacket(byte[] data, String device_address){
        this.indx = getIndex(data);
        this.length = getLength(data);
        this.flags = getFlags(data);
        this.last = (this.flags & FLAG_LAST) == FLAG_LAST;
        this.data = ByteWork.getBytesSize(data, protocol_offset, this.length);
        this.address = device_address;
    }
    //This is used to activate notifications
    //TODO: needs protocol ID and so on
    public BlePacket(byte[] data){
        this.data = data;
    }

    public void setData(byte[] d) {this.data = d ;}
    public byte[] getData(){ return data; }
    public String getAddress(){ return address; }

    public boolean isLast() {return this.last ; }

    public void setAddress(String a){ address = a; }

    public int getLength()
    {
        return this.length;
    }
    public static byte[] fromArray(ArrayList<BlePacket> m_frag) {
        int total_lengh = 0;
        for(BlePacket blePacket: m_frag){
            total_lengh+=blePacket.getLength();
        }
        //results
        byte[] result = new byte[total_lengh];
        //special case for zero
        byte[] zero = m_frag.get(0).getData();
        System.arraycopy(zero, 0, result, 0, zero.length);
        for(int i = 1 ; i < m_frag.size(); i++){
            byte [] a = m_frag.get(i).getData();
            System.arraycopy(a, 0, result, m_frag.get(i-1).getData().length, a.length);
        }
        Log.e("BLE PKT: ", "data.length " + result.length);
        return  result;
    }


    //FIXME: this is hardcoded values
    private int getIndex(byte[] data)
    {
        return  ByteWork.convertByteToInt(ByteWork.getBytes(data, 0, 1)[0]);
    }
    private int getLength(byte[] data)
    {
        return ByteWork.convertByteToInt(ByteWork.getBytes(data, 1, 2)[0]);
    }
    private int getFlags(byte[] data)
    {
        return ByteWork.convertByteToInt(ByteWork.getBytes(data, 2, 3)[0]);
    }
    @Override
    public String toString() {
        return "BLE PKT[{from: "+this.address+
                ", indx: " + this.indx+
                ", length: " + this.length+
                ", last: " + this.last + "]";
    }
}
