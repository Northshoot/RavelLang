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
    private final static int LENGTH = 1;
    private final static int FLAGS = 2;
    private final static int protocol_offset = 3;
    //Packet Structure
    private int indx=0;
    private int length=0;
    private int flags = 0;
    private byte[] data;
    //addons not serializible to out/in
    private String address;
    private boolean last = false;
    public static final int FLAG_LAST = 1;


    public BlePacket(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        this(characteristic.getValue(), gatt.getDevice().getAddress());
    }

    private final static String TAG = BlePacket.class.getSimpleName();
    private BlePacket(byte[] data, String device_address){
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
    private void setLast(int l) {this.flags = l; this.last = (FLAG_LAST & this.flags) == FLAG_LAST; }
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
                indx++;
                data_length -=m_max_pkt_length;
            } else {
                blePacket.setLast(1);
                if (data.length > data_length) {
                    byte[] chunk = new byte[data_length];
                    System.arraycopy(data, indx*m_max_pkt_length, chunk, 0, data_length);
                    blePacket.setData(chunk);
                } else {
                    blePacket.setData(data);
                }
                blePacket.setLength(data_length);
                hasFragment = false;
            }
            mPacketList.add(blePacket);
        }
        return mPacketList;
    }

    public static BlePacket packetToNetwork(byte[] data, int indx, int flags)
    {
        BlePacket blePacket = BlePacket.emptyPacket();
        blePacket.indx = indx;
        blePacket.length = data.length + BleDefines.BLE_FRAGMENT_HEADER_LENGTH;
        blePacket.flags = flags;
        blePacket.last = true;
        blePacket.data = data;

        return blePacket;
    }
    public static BlePacket packetFromNetworkBytes(byte[] data){
        BlePacket blePacket = BlePacket.emptyPacket();
        blePacket.setIndx(getIndex(data));
        blePacket.setLength(getLength(data));
        blePacket.setLast(getFlags(data));
        blePacket.setData(data);

        return blePacket;
    }
    private BlePacket() {}

    private static BlePacket emptyPacket() {
        return new BlePacket();
    }

    public static byte[] toPacketByteArray(BlePacket blePacket){
        Log.d(TAG, "converting to bytes");
        int total_pkt_lenght = blePacket.length+3;
        byte[] pkt = new byte[blePacket.length+3];

        pkt[INDEX] = (byte) blePacket.indx;
        pkt[LENGTH] = (byte) blePacket.length;
        pkt[FLAGS] = (byte) blePacket.flags;


        System.arraycopy( blePacket.getData(), 0, pkt, protocol_offset, blePacket.getData().length );
        Log.d(TAG, "Converting pkt to byte[" + pkt.length + "]");
        return pkt;
    }
    //FIXME: this is hardcoded values
    private static int getIndex(byte[] data)
    {
        return  ByteWork.convertByteToInt(ByteWork.getBytes(data, INDEX, INDEX+1)[0]);
    }
    private static int getLength(byte[] data)
    {
        return ByteWork.convertByteToInt(ByteWork.getBytes(data, LENGTH, LENGTH+1)[0]);
    }
    private static int getFlags(byte[] data)
    {
        return ByteWork.convertByteToInt(ByteWork.getBytes(data, FLAGS, FLAGS+1)[0]);
    }
    private static byte[] getdata(byte[] data)
    {
        //data is data.length - header 3 bytes
        return  ByteWork.getBytes(data, protocol_offset, data.length);
    }
    @Override
    public String toString() {
        return "BLE PKT[{from: "+this.address+
                ", indx: " + this.indx+
                ", length: " + this.length+
                ", last: " + this.last + "]";
    }


}
