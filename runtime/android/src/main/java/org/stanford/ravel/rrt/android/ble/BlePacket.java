package org.stanford.ravel.rrt.android.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import org.stanford.ravel.rrt.android.utils.ByteWork;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by lauril on 3/8/17.
 */

public class BlePacket implements Serializable {

    private final static int INDEX = 0;
    private final static int LENGTH = 0;
    private final static int FLAGS = 0;

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

    private void setIndx(int d) {this.indx = d ;}
    private void setLast(int l) {this.flags = l; }
    private void setLength(int l) {this.length = l; }
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

    public static ArrayList<BlePacket> packetsFromBytes(byte[] data){
        ArrayList<BlePacket> mPacketList = new ArrayList<>();
        boolean hasFragment = true;
        int data_length = data.length;
        int indx=0;
        int m_max_pkt_length = BleDefines.BLE_MAX_DATA_LENGTH-BleDefines.BLE_FRAGMENT_HEADER_LENGTH;
        while(hasFragment){
            BlePacket blePacket = BlePacket.emptyPacket();
            blePacket.setIndx(indx);
            if(data_length > m_max_pkt_length){
                //more data then it fits to the packet
                blePacket.setLast(0);
                blePacket.setLength(m_max_pkt_length);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(data, indx*m_max_pkt_length, m_max_pkt_length );
                blePacket.setData(bos.toByteArray());
            } else {
                blePacket.setLast(1);
                blePacket.setData(data);
                blePacket.setLength(data_length);
                hasFragment = false;
            }

        }
        return mPacketList;
    }

    public static BlePacket packetFromBytes(byte[] data, int flag){
        assert (data.length <= BleDefines.BLE_MAX_DATA_LENGTH-BleDefines.BLE_FRAGMENT_HEADER_LENGTH);
        BlePacket blePacket = BlePacket.emptyPacket();
        blePacket.setLast(flag);
        blePacket.setData(data);
        blePacket.setLength(data.length);
        return blePacket;
    }

    public static BlePacket packetFromBytes(byte[] data){
        return packetFromBytes(data, 1);
    }
    private BlePacket() {}

    private static BlePacket emptyPacket() {
        return new BlePacket();
    }

    public static byte[] toPacketByteArray(BlePacket blePacket){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[]    indx = new byte[1];
        indx[0] = (byte) blePacket.indx;
        bos.write(indx, INDEX, 1 );

        byte[]    length = new byte[1];
        length[0] = (byte) blePacket.length;
        bos.write(length, LENGTH, 1 );

        byte[]    flags   = new byte[1];
        flags[0] = (byte) blePacket.flags;
        bos.write(length, FLAGS, 1 );

        byte[]  data    = blePacket.getData();
        bos.write(data, LENGTH+1, data.length);
        return bos.toByteArray();
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
