package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.lang.Intrinsic;
import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.utils.GrowableByteArray;

/**
 * Created by lauril on 1/25/17.
 */
public class RavelPacket {
    private final static int SRC = 0; // 8 bits for source
    private final static int DST = 1; // 8 bits for destination
    private final static int FLAGS = 2; // 8 bits for flags

    private final static int RESERVED = 3; // reserved for byte mapping

    public static class Flags {
        public static final int FLAG_ACK = 1;
        public static final int FLAG_SAVE_DONE = 4;
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
    private byte src = -1;

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
    private boolean ack = false;
    private boolean save_done = false;

    private RavelPacket(byte[] data, boolean isNetwork) {
        //unmangle data
        if (isNetwork) {
            this.src = data[SRC];
            this.dst = data[DST];
            this.flags = data[FLAGS];
            this.ack = (this.flags & Flags.FLAG_ACK) == Flags.FLAG_ACK;
            this.save_done = (this.flags & Flags.FLAG_SAVE_DONE) == Flags.FLAG_SAVE_DONE;

            this.record_end = data.length;
            this.record_data = ByteWork.getBytes(data, RESERVED, record_end);
            //Record can not be less than model_id, record_id, and a byte
            if (record_data.length <3) throw new AssertionError("RavelPacket: Expected at least 3 bytes");
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

    private void setSaveDone() {
        flags |= Flags.FLAG_SAVE_DONE;
        save_done = true;
    }

    private static int getModelIdFromRecord(byte[] data) {
        return (int)data[BYTE_POS_MODEL_ID] & 0xFF;
    }

    private static int getRecordIdFromRecord(byte[] data) {
        if (data.length <3) throw new AssertionError("getRecordIdFromRecord: Expected at least 3 bytes");
        return (int)((data[2] & 0xFF) << 8 | (data[1] & 0xFF));
    }

    public void setSourceDestination(int source, int destination) {
        assert source < 255;
        assert source >= 0;
        assert destination < 255;
        assert destination >= 0;
        src = (byte)source;
        dst = (byte)destination;
    }

    public int getSource() {
        return (int)src & 0xFF;
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
        outputStream.write_byte(src);
        outputStream.write_byte(dst);
        outputStream.write_byte((byte)this.flags);
        outputStream.write(record_data, 0, record_data.length);
        return outputStream.toByteArray();
    }

    public boolean isAck() {
        return ack;
    }
    public boolean isSaveDone() {
        return save_done;
    }

    @Override
    public String toString() {
        return "[SRC: " + this.src + ", DST: " + this.dst
                +", MODEL_ID: " + this.model_id + " rec_idx: " + this.record_id + "]";
    }
}
