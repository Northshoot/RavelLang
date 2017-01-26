package patterns.src.java.rrt;

import patterns.src.java.utils.ByteWork;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {

    public final static int SRC = 4; //32 bits for source
    public final static int DST = 8; //32 bits for destination
    public final static int PARTIAL = 9; //1 bit to note partial
    public final static int LAST = 65; //1 bit to note end partial
    public final static int RESERVED = 9; // reserved for byte mapping
    public final static int MODEL_ID = 10; //8 bits for model id




    public int model_id;
    public byte[] record_data;
    public int dst;
    public int src;
    public int partial;
    public int last;


    private byte[] mData;

    public RavelPacket(byte[] data){
        this.mData = data;
        //unmangle data
        this.src = ByteWork.convertFourUnsignedBytesToInt(ByteWork.getBytes(data, 0, SRC));
        this.dst = ByteWork.convertFourUnsignedBytesToInt(ByteWork.getBytes(data, SRC, DST));
        byte[] pkt =  ByteWork.getBytes(data, DST, RESERVED);
        this.partial = (pkt[0] >> 0) & 1;
        this.last = (pkt[0] >> 1) & 1;
        this.model_id = ByteWork.getBytes(data, RESERVED,MODEL_ID)[0];
        this.record_data = ByteWork.getBytes(data, MODEL_ID, data.length);
    }

    public boolean isLast(){
        return this.last == 1;
    }

    public boolean isPartial(){
        return partial == 1 ;
    }
    @Override
    public String toString() {
        return "[SRC: " + this.src + ", DST: " + this.dst
                + ", PARTIAL: "+ isPartial() + ", LAST: "+ isLast()
                +", MODEL_ID: " + this.model_id + ", DATA: " + ByteWork.bytesToHex(this.mData) + "]";
    }
}
