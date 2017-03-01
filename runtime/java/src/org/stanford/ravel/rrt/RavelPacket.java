package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.utils.GrowableByteArray;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {
    private final static int SRC = 1; // 8 bits for source
    private final static int DST = 2; // 8 bits for destination
    private final static int RESERVED = 3; // reserved for byte mapping

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
    public byte dst=-1;
    public byte src=-1;

    //TODO: add packetization
    private int flags = 0;
    private boolean partial = false;
    private boolean last = false;
    private boolean ack = false;

    private RavelPacket(byte[] data, boolean isNetwork) {
        //unmangle data
        if (isNetwork) {
            this.src = data[0];
            this.dst = data[1];
            this.flags = data[2];
            this.partial = (this.flags & Flags.FLAG_PARTIAL) == Flags.FLAG_PARTIAL;
            this.last = (this.flags & Flags.FLAG_LAST) == Flags.FLAG_LAST;
            this.ack = (this.flags & Flags.FLAG_ACK) == Flags.FLAG_ACK;

            this.record_end = data.length;
            this.record_data = ByteWork.getBytes(data, RESERVED, record_end);
            this.model_id = getModelIdFromRecord(this.record_data);
            this.record_id = getRecordIdFromRecord(this.record_data);
        } else {
            this.record_data = data;
            this.record_end = record_data.length + RESERVED;
            this.model_id = getModelIdFromRecord(this.record_data);
            this.record_id = getRecordIdFromRecord(this.record_data);
        }
    }

    private RavelPacket(int recordSize, int modelId, int recordId) {
        this.record_end = recordSize + RESERVED;
        this.record_data = new byte[recordSize];
        System.arraycopy(ByteWork.getByteArray(modelId), 0, this.record_data, 0, 4);
        System.arraycopy(ByteWork.getByteArray(recordId), 0, this.record_data, 4, 4);
        this.model_id = modelId;
        this.record_id = recordId;
    }

    private RavelPacket(int modelId, int recordId) {
        this.record_end = 8 + RESERVED;
        this.record_data = new byte[8];
        System.arraycopy(ByteWork.getByteArray(modelId), 0, this.record_data, 0, 4);
        System.arraycopy(ByteWork.getByteArray(recordId), 0, this.record_data, 4, 4);
        this.model_id = modelId;
        this.record_id = recordId;
    }

    public static RavelPacket fromRecord(byte[] recordData) {
        return new RavelPacket(recordData, false);
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
        return new RavelPacket(data, true);
    }

    private void setAck() {
        flags |= Flags.FLAG_ACK;
        ack = true;
    }

    private static int getModelIdFromRecord(byte[] data) {
        return data[0];
    }
    private static int getRecordIdFromRecord(byte[] data) {
        return data[1];
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
        outputStream.write_byte(src);
        outputStream.write_byte(dst);
        outputStream.write_byte((byte)this.flags);
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
