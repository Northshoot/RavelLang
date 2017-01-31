package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.utils.ByteWork;
import patterns.src.java.model.Model;

import java.io.ByteArrayOutputStream;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {

    public final static int SRC = 4; //32 bits for source
    public final static int DST = 8; //32 bits for destination
    public final static int RESERVED = 9; // reserved for byte mapping
    public final static int RECORD_DATA = 9+Model.RECORD_SIZE; // record data



    public int model_id =-1;
    public byte[] record_data=null;
    public int dst=-1;
    public int src=-1;
    //TODO: add packetization
    public byte reserved=-1;
    public int partial=-1;
    public int last=-1;



    private byte[] __in_mData;
    private byte[] mData;



    public RavelPacket(){
        this.record_data = new byte[Model.RECORD_SIZE];
    }

    public int getSize(){
        return RECORD_DATA;
    }
    void pprint(String s){
        //TODO
        System.out.println("[RavelPacket::]>" + s);
    }
    public void fromRecord(byte [] data){
        //unmangle data
        pprint("fromRecord: " + data.length);
        this.__in_mData = data;
        this.mData = this.__in_mData;
        this.model_id =getModelIdFromRecord(data);
        this.record_data = new byte[data.length];
        this.record_data = data;
    }

    public void fromNetwork(byte[] data){
        //unmangle data
        System.out.println("DATA: " + data.length);
        this.__in_mData = data;
        this.src = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 0, SRC));
        this.dst = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, SRC, DST));
        this.reserved =  ByteWork.getBytes(data, DST, RESERVED)[0];
        this.partial = (this.reserved >> 0) & 1;
        this.last = (this.reserved >> 1) & 1;
        this.record_data = ByteWork.getBytes(data, RESERVED, RECORD_DATA);
        this.model_id = getModelIdFromRecord(this.record_data);
        this.mData = this.__in_mData;
    }

    private int getModelIdFromRecord(byte[] data){
        //TODO: no hardcoded vals
        return ByteWork.convertFourBytesToInt(
                ByteWork.getBytes(data, 0, 4)
        );
    }



    public byte[] toBytes(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(src);
        outputStream.write(dst);
        outputStream.write(getPartial());
        outputStream.write(record_data, 0, record_data.length);

        //AUTOGEN END
        return outputStream.toByteArray();
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
                +", MODEL_ID: " + this.model_id + "]";
    }

    private byte getPartial() {
        if (isLast() && isPartial() ) {
            this.partial = 3;
        } else if (isLast() && !isPartial()){
            this.partial = 1;
        } else if (!isLast() && isPartial()){
            this.partial = 1;
        } else {
            this.partial =0;
        }
        return reserved;
    }
}
