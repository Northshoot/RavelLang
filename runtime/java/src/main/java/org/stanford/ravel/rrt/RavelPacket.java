package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.lang.Intrinsic;
import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.utils.GrowableByteArray;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {
    private final static int TIER = 0; // 8 bits for source TODO: this is tier ID
    private final static int SRC = 1; // 32 bits for source TODO: source
    private final static int DST = 5; // 8 bits for destination
    private final static int FLAGS = 6; // 8 bits for flags

    private final static int RESERVED = 7; // reserved for byte mapping

    public static class Flags {
        public static final int FLAG_ACK = 1;
        public static final int FLAG_SAVE_DONE = 4;
        public static final int FLAG_DELETE = 8;
    }

    /**
     * The minimum length of a RavelPacket
     */
    public static final int MIN_LENGTH = RESERVED;

    public final int model_id;
    public final int record_id;
    private final byte[] record_data;
    private final int record_end;
    private byte dst = -1;
    private int src = -1;
    private byte tier = -1;

    private static int BYTE_POS_MODEL_ID = 0;

//    //TODO: add packetization

//    Caused by: java.lang.ArrayIndexOutOfBoundsException: length=2; index=2
//    at org.stanford.ravel.rrt.RavelPacket.getRecordIdFromRecord(RavelPacket.java:122)
//    at org.stanford.ravel.rrt.RavelPacket.<init>(RavelPacket.java:56)
//    at org.stanford.ravel.rrt.RavelPacket.fromNetwork(RavelPacket.java:105)
//    at org.stanford.ravel.rrt.android.AndroidDriver.packetCompleted(AndroidDriver.java:62)
//    at org.stanford.ravel.rrt.android.AndroidDriver.fragment_arrived(AndroidDriver.java:81)
//    at org.stanford.ravel.rrt.android.AndroidDriver$1.onReceive(AndroidDriver.java:115)

    private int flags = 0;

    private RavelPacket(byte[] data, boolean isNetwork) {
        //unmangle data
        if (isNetwork) {
            this.tier = data[TIER];
            this.src = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data,SRC, DST));
            this.dst = data[DST];
            this.flags = data[FLAGS];

            this.record_end = data.length;
            this.record_data = ByteWork.getBytes(data, RESERVED, record_end);
            //Record can not be less than model_id, record_id, and a byte
            if (record_data.length < 3) throw new AssertionError("RavelPacket: Expected at least 3 bytes");
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
        this.record_data[0] = (byte)modelId;
        Intrinsic.write_uint16(this.record_data, 1, recordId);
        this.model_id = modelId;
        this.record_id = recordId;
    }

    private RavelPacket(int modelId, int recordId) {
        this.record_end = 3 + RESERVED;
        this.record_data = new byte[3];
        this.record_data[0] = (byte)modelId;
        Intrinsic.write_uint16(this.record_data, 1, recordId);
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

    public static RavelPacket makeSaveDone(int modelId, int recordId) {
        RavelPacket pkt = new RavelPacket(modelId, recordId);
        pkt.setSaveDone();
        return pkt;
    }

    public static RavelPacket makeDelete(int modelId, int recordId) {
        RavelPacket pkt = new RavelPacket(modelId, recordId);
        pkt.setDelete();
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
    }

    private void setSaveDone() {
        flags |= Flags.FLAG_SAVE_DONE;
    }

    private void setDelete() {
        flags |= Flags.FLAG_DELETE;
    }

    private static int getModelIdFromRecord(byte[] data) {
        return (int)data[BYTE_POS_MODEL_ID] & 0xFF;
    }

    private static int getRecordIdFromRecord(byte[] data) {
        if (data.length < 3) throw new AssertionError("getRecordIdFromRecord: Expected at least 3 bytes");
        return (int)((data[2] & 0xFF) << 8 | (data[1] & 0xFF));
    }

    public void setSourceDestination(int source, int tier, int destination) {
        //TODO expand addressing
        assert tier < 255;
        assert tier >= 0;
        assert destination < 255;
        assert destination >= 0;
        this.src = source;
        this.tier = (byte)tier;
        this.dst = (byte)destination;
    }

    public int getSource() {
        return src;
    }

    public int getTier() {
        return (int)tier & 0xFF;
    }
    public int getDestination() {
        return (int)dst & 0xFF;
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
        outputStream.write_byte(tier);
        outputStream.write_int32(src);
        outputStream.write_byte(dst);
        outputStream.write_byte((byte)this.flags);
        outputStream.write(record_data, 0, record_data.length);
        return outputStream.toByteArray();
    }

    public boolean isAck() {
        return (flags & Flags.FLAG_ACK) == Flags.FLAG_ACK;
    }
    public boolean isSaveDone() {
        return (flags & Flags.FLAG_SAVE_DONE) == Flags.FLAG_SAVE_DONE;
    }
    public boolean isDelete() {
        return (flags & Flags.FLAG_DELETE) == Flags.FLAG_DELETE;
    }

    @Override
    public String toString() {
        return "[TIER: " + this.tier + ", SRC: " + this.src + ", DST: " + this.dst
                +", MODEL_ID: " + this.model_id + " rec_idx: " + this.record_id + "]";
    }
}
