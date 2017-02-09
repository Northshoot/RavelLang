package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.utils.GrowableByteArray;

import java.nio.ByteBuffer;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {
    private final static int SRC = 4; // 32 bits for source
    private final static int DST = 8; // 32 bits for destination
    private final static int RESERVED = 12; // reserved for byte mapping

    public static class Flags {
        public static final int FLAG_PARTIAL = 1;
        public static final int FLAG_LAST = 2;
        public static final int FLAG_ACK = 4;
    }

    /**
     * The minimum length of a RavelPacket
     */
    public static final int MIN_LENGTH = RESERVED;

    public final int model_id;
    public final int record_id;
    private final byte[] record_data;
    private final int record_end;
    public int dst=-1;
    public int src=-1;

    //TODO: add packetization
    private int flags = 0;
    private boolean partial = false;
    private boolean last = false;
    private boolean ack = false;

    private RavelPacket(byte[] data) {
        //unmangle data
        this.src = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 0, SRC));
        this.dst = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, SRC, DST));

        this.flags =  ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, DST, RESERVED));
        this.partial = (this.flags & Flags.FLAG_PARTIAL) == Flags.FLAG_PARTIAL;
        this.last = (this.flags & Flags.FLAG_LAST) == Flags.FLAG_LAST;
        this.ack = (this.flags & Flags.FLAG_ACK) == Flags.FLAG_ACK;

        this.record_end = data.length;
        this.record_data = ByteWork.getBytes(data, RESERVED, record_end);
        this.model_id = getModelIdFromRecord(this.record_data);
        this.record_id = getRecordIdFromRecord(this.record_data);
    }

    private RavelPacket(int recordSize, int modelId, int recordId) {
        this.record_end = recordSize + RESERVED;
        this.record_data = new byte[recordSize];
        System.arraycopy(ByteWork.getByteArray(modelId), 0, this.record_data, 0, 4);
        System.arraycopy(ByteWork.getByteArray(recordId), 0, this.record_data, 4, 4);
        this.model_id = modelId;
        this.record_id = recordId;
    }

    private RavelPacket(ModelRecord record) {
        this.record_data = record.toBytes();
        this.record_end = record_data.length + RESERVED;
        this.model_id = getModelIdFromRecord(this.record_data);
        this.record_id = getRecordIdFromRecord(this.record_data);
    }

    private RavelPacket(int modelId, int recordId) {
        this.record_end = 8 + RESERVED;
        this.record_data = new byte[8];
        System.arraycopy(ByteWork.getByteArray(modelId), 0, this.record_data, 0, 4);
        System.arraycopy(ByteWork.getByteArray(recordId), 0, this.record_data, 4, 4);
        this.model_id = modelId;
        this.record_id = recordId;
    }

    public static RavelPacket fromRecord(ModelRecord record) {
        return new RavelPacket(record);
    }

    public static RavelPacket makeAck(int modelId, int recordId) {
        RavelPacket pkt = new RavelPacket(modelId, recordId);
        pkt.setAck();
        return pkt;
    }

    // For testing only
    public static RavelPacket empty(int recordSize, int modelId, int recordId) {
        return new RavelPacket(recordSize, modelId, recordId);
    }

    public static RavelPacket fromNetwork(byte[] data) {
        return new RavelPacket(data);
    }

    private void setAck() {
        flags |= Flags.FLAG_ACK;
        ack = true;
    }

    private static int getModelIdFromRecord(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(
                ByteWork.getBytes(data, 0, 4));
        return buffer.getInt();
    }
    private static int getRecordIdFromRecord(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(
                ByteWork.getBytes(data, 4, 8));
        return buffer.getInt();
    }

    public byte[] getRecordData() {
        return record_data;
    }

    public int getSize() {
        return record_end;
    }
    public int getDataSize() {
        return record_end - RESERVED;
    }

    public byte[] toBytes() {
        GrowableByteArray outputStream = new GrowableByteArray();
        outputStream.write(ByteWork.getByteArray(src));
        outputStream.write(ByteWork.getByteArray(dst));
        outputStream.write(ByteWork.getByteArray(this.flags));
        outputStream.write(record_data, 0, record_data.length);
        return outputStream.toByteArray();
    }

    public boolean isLast() {
        return last;
    }
    public boolean isPartial() {
        return partial;
    }
    public boolean isAck() {
        return ack;
    }

    @Override
    public String toString() {
        return "[SRC: " + this.src + ", DST: " + this.dst
                + ", PARTIAL: "+ isPartial() + ", LAST: "+ isLast()
                +", MODEL_ID: " + this.model_id + " rec_idx: " + this.record_id + "]";
    }
}
