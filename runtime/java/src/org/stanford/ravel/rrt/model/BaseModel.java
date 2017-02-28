package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.events.RunnableEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for generated models, containing code to track acks and
 * packets in flight
 *
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseModel<RecordType extends ModelRecord> implements ModelQuery<RecordType>, ModelBottomAPI, ModelCommandAPI<RecordType> {
    private static class RecordState {
        boolean inRest = false;
        boolean inUse = false;
        int acks = 0;
    }

    private final DispatcherAPI mDispatcher;
    private final RecordState[] stateArray;
    private final ArrayList<RecordType> mRecords = new ArrayList<>();
    private int currentPos = 0;

    private int mModelSize;
    BaseModel(DispatcherAPI dispatcher, int size) {
        mModelSize = size;
        mRecords.ensureCapacity(size);
        mDispatcher = dispatcher;
        stateArray = new RecordState[size];
        for (int i = 0; i < stateArray.length; i++)
            stateArray[i] = new RecordState();
    }

    // the generated methods for dispatching events
    protected abstract void notifyFull(Context<RecordType> ctx);
    protected abstract void notifyArrived(Context<RecordType> ctx);
    protected abstract void notifyDeparted(Context<RecordType> ctx);
    protected abstract void notifySaveDone(Context<RecordType> ctx);

    // the generated methods for marshalling/unmarshalling
    protected abstract byte[] marshall(RecordType record, Endpoint endpoint);
    protected abstract RecordType unmarshall(byte[] data, Endpoint endpoint);

    void markRecordAtRest(int record) {
        stateArray[record].inRest = true;
    }
    void markRecordArrived(int record) {
        stateArray[record].acks = 0;
    }
    void markRecordInUse(int record) {
        stateArray[record].inUse = true;
    }
    void markRecordReleasedFromUse(int record) {
        stateArray[record].inUse = false;
    }
    private void requireRecordAcks(int record, int acks) {
        stateArray[record].acks = acks;
    }
    boolean recordAck(int record) {
        stateArray[record].acks--;
        return (stateArray[record].acks == 0);
    }
    private boolean safeToDeleteRecord(int record){
        RecordState rec = stateArray[record];
        return !rec.inUse && rec.acks == 0;
    }

    private boolean tryAddRecord(RecordType record) {
        if (currentPos >= mModelSize )
            return false;
        // TODO: if durable save to disk

        int idx = currentPos++;
        record.index(idx);
        mRecords.add(idx, record);
        return true;
    }

    void pprint(String s){
        System.out.println("[BaseModel::]>" + s);
    }

    private void queueFullEvent() {
        mDispatcher.queueEvent(new RunnableEvent() {
            @Override
            public void run() {
                Context<RecordType> ctx = new Context<>(BaseModel.this, Error.OUT_OF_STORAGE);
                notifyFull(ctx);
            }
        });
    }

    Context<RecordType> addRecord(RecordType record) {
        if (!tryAddRecord(record))
            return new Context<>(this, Error.OUT_OF_STORAGE);

        if (currentPos == mModelSize)
            queueFullEvent();

        return new Context<>(this, record);
    }

    Error sendOneRecord(RavelPacket pkt, Endpoint e) {
        return mDispatcher.model__sendData(pkt, e);
    }

    Context<RecordType> sendRecord(RecordType record, Collection<String> endpointNames) {
        Collection<Endpoint> endpoints = new ArrayList<>();
        for (String name : endpointNames)
            endpoints.addAll(mDispatcher.getEndpointsByName(name));

        Error error = Error.SUCCESS;
        requireRecordAcks(record.index(), endpoints.size());
        for (Endpoint e : endpoints) {
            RavelPacket pkt = RavelPacket.fromRecord(marshall(record, e));

            Error error2 = sendOneRecord(pkt, e);
            if ((error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS) || error == Error.SUCCESS)
                error = error2;
        }
        if (error != Error.IN_TRANSIT && error != Error.SUCCESS)
            return new Context<>(this, error);
        else
            return new Context<>(this, record);
    }

    abstract void recordAcknowledged(int recordId);

    @Override
    public void recordArrived(RavelPacket pkt, Endpoint endpoint) {
        if (pkt.isAck()) {
            // we received an ack, count it towards the number of acks
            // we expected, and if we reach zero tell the subclass to do something

            // FIXME duplicate acks?
            if (recordAck(pkt.record_id)) {
                pprint("record fully acknowledged");
                recordAcknowledged(pkt.record_id);
            }
            return;
        }

        pprint("record_arrived");

        // Let the controllers and local model deal with it first...
        Context<RecordType> ctx = new Context<>(this);
        RecordType record = unmarshall(pkt.getRecordData(), endpoint);
        addRecord(record);
        ctx.record = record;
        //notify all subscribers
        notifyArrived(ctx);

        // then send an ack back to the endpoint where this came from
        //
        // FIXME: combine ack with packet in the opposite direction?
        mDispatcher.model__sendData(RavelPacket.makeAck(pkt.model_id, pkt.record_id), endpoint);
    }

    /**
     * Call from the driver layers with a record
     * @param pkt
     * @param endpoint
     */
    @Override
    public void recordDeparted(RavelPacket pkt, Endpoint endpoint) {
        if (pkt.isAck()) // we sent an ACK, great no need to fuss about it...
            return;

        //normal data
        pprint("record_departed");
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = get(pkt.record_id);
        //notify all subscribers
        notifyDeparted(ctx);
    }

    @Override
    public void record_saved_durably(RavelPacket pkt) {
        //TODO: only true do remote and durable
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = get(pkt.record_id);

        // mark saved durably
        // notify all subscribers
        notifySaveDone(ctx);
    }

    @Override
    public void record_saved_endpoint(RavelPacket pkt, Endpoint endpoint) {
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = get(pkt.record_id);

        //notify all subscribers
        notifySaveDone(ctx);
    }

    @Override
    public void endpoint_connected(Endpoint endpoint) {
        // TODO
    }

    /***************************************************************/
    /*************** Model Command API implementation **************/
    /***************************************************************/

    public Context<RecordType> delete(int deleteField) {
        if (deleteField > currentPos || deleteField < 0)
            throw new IndexOutOfBoundsException();
        // take a copy for return
        RecordType mDeletedRecord = mRecords.get(deleteField);

        // delete from local array
        for(int i = deleteField ; i < currentPos-1;i++) {
            mRecords.set(i, mRecords.get(i + 1));
            mRecords.get(i).index(i);
        }
        currentPos--;
        return new Context<>(this, mDeletedRecord);
    }

    /***************************************************************/
    /*************** Model Query API implementation ***************/
    /***************************************************************/
    @Override
    public RecordType first() {
        return mRecords.get(0);
    }

    @Override
    public RecordType last() {
        return mRecords.get(currentPos);
    }

    @Override
    public RecordType get(int x) {
        return mRecords.get(x);
    }

    @Override
    public RecordType[] all(RecordType[] unused) {
        return mRecords.toArray(unused);
    }

    @Override
    public void clear() {
        mRecords.clear();
        currentPos = 0;
    }
}
