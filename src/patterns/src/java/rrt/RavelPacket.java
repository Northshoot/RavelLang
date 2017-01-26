package patterns.src.java.rrt;

import patterns.src.java.utils.ByteWork;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {

    public final static int SRC = 31; //32 bits for source
    public final static int DST = 63; //32 bits for destination
    public final static int PARTIAL = 64; //1 bit to note partial
    public final static int LAST = 65; //1 bit to note end partial
    public final static int MODEL_ID = 73; //8 bits for model id




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
        this.src = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 0, SRC));
        this.dst = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, SRC+1, DST));
        this.partial = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, PARTIAL+1,LAST));
        this.partial = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, LAST+1,MODEL_ID));
        this.record_data = ByteWork.getBytes(data, MODEL_ID+1, data.length);
    }

    @Override
    public String toString() {
        return "[SRC: " + this.src + ", DST: " + this.dst
                + "PARTIAL: ,"+ this.partial + ", LAST: "+ this.last
                +", MODEL_ID: " + this.model_id + ", DATA: " + ByteWork.bytesToHex(this.mData) + "]";
    }
}
