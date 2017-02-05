package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.utils.ByteWork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {

    public final static int SRC = 4; // 32 bits for source
    public final static int DST = 8; // 32 bits for destination
    public final static int RESERVED = 12; // reserved for byte mapping

    public int model_id =-1;
    public byte[] record_data=null;
    public int dst=-1;
    public int src=-1;
    //TODO: add packetization
    public int reserved=-1;
    public int partial=-1;
    public int last=-1;
    private int record_end = 0;

    private byte[] mData;

    public int getSize() {
        return record_end;
    }

    public static RavelPacket fromRecord(byte[] data) {
        // unmangle data
        RavelPacket blank = new RavelPacket(data.length);
        blank.mData = data;
        blank.record_data = data;
        blank.model_id = getModelIdFromRecord(blank.record_data);
        return blank;
    }

    private RavelPacket(int recordSize) {
        this.record_end = recordSize + RESERVED;
        this.record_data = new byte[recordSize];
    }

    // For testing only
    public static RavelPacket empty(int recordSize) {
        return new RavelPacket(recordSize);
    }

    public static RavelPacket fromNetwork(byte[] data) {
        return new RavelPacket(data);
    }

    private RavelPacket(byte[] data) {
        //unmangle data
        this.src = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 0, SRC));
        this.dst = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, SRC, DST));
        this.reserved =  ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, DST, RESERVED));
        this.partial = (this.reserved >> 0) & 1;
        this.last = (this.reserved >> 1) & 1;
        this.record_end = data.length;
        this.record_data = ByteWork.getBytes(data, RESERVED, record_end);
        this.model_id = getModelIdFromRecord(this.record_data);
        this.mData = data;
    }

    private static int getModelIdFromRecord(byte[] data){
        //TODO: no hardcoded vals

        ByteBuffer buffer = ByteBuffer.wrap(
                ByteWork.getBytes(data, 0, 4));
        return buffer.getInt();
    }
    private int getRecordIdx(){
        //TODO: no hardcoded vals

        ByteBuffer buffer = ByteBuffer.wrap(
                ByteWork.getBytes(this.record_data, 4, 8));
        return buffer.getInt();
    }


    public byte[] toBytes(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(ByteWork.getByteArray(src));
            outputStream.write(ByteWork.getByteArray(dst));
            outputStream.write(ByteWork.getByteArray(getPartial()));
            outputStream.write(record_data, 0, record_data.length);
            //AUTOGEN END
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                +", MODEL_ID: " + this.model_id + " rec_idx: " + getRecordIdx() + "]";
    }

    private int getPartial() {
        if (isLast() && isPartial() ) {
            this.reserved = 3;
        } else if (isLast() && !isPartial()){
            this.reserved = 1;
        } else if (!isLast() && isPartial()){
            this.reserved = 1;
        } else {
            this.reserved =0;
        }
        return reserved;
    }
}
