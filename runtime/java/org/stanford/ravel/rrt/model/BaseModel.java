package org.stanford.ravel.rrt.model;

import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.rrt.*;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import patterns.src.java.app.AppDispatcher;

import java.util.ArrayList;

/**
 * Base class for generated models, containing code to track acks and
 * packets in flight
 *
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseModel<RecordType> implements ModelQuery<RecordType>, ModelBottomAPI, ModelCommandAPI<RecordType> {
    private static class RecordState {
        boolean inRest = false;
        boolean inTransit = false;
        boolean inUse = false;
        boolean ack = false;
    }

    protected final DispatcherAPI mDispatcher;
    private final RecordState[] stateArray;
    private final ArrayList<RecordType> mRecords = new ArrayList<RecordType>();
    private int currentPos = 0;

    private int mModelSize;
    protected BaseModel(DispatcherAPI dispatcher, int size) {
        mModelSize = size;
        mRecords.ensureCapacity(size);
        mDispatcher = dispatcher;
        stateArray = new RecordState[size];
    }


    /******************* ******* event queue ***************************/
    QueueArray<Event> eventQueue = new QueueArray<>();

    protected synchronized void runNextEvent(){
        try {
            Event e = eventQueue.dequeue();

            switch (e.getType()) {
                case MODEL__NOTIFY_FULL:
                    notifyFull((Context<RecordType>) ((ModelEvent)e).ctx);
                    break;

            }
        } catch (java.util.NoSuchElementException e){
            pprint("No events to process");
        }
    }


    private void post_task(){
        new Thread(() -> runNextEvent()).start();
    }

    // the generated methods for dispatching events
    protected abstract void notifyFull(Context<RecordType> ctx);
    protected abstract void notifyArrived(Context<RecordType> ctx);
    protected abstract void notifyDeparted(Context<RecordType> ctx);
    protected abstract void notifySaveDone(Context<RecordType> ctx);

    // the generated methods for marshalling/unmarshalling
    protected abstract RecordType unmarshall(byte[] data);

    private void markRecordAtRest(int record) {
        stateArray[record].inRest = true;
    }
    private void markRecordInTransit(int record) {
        stateArray[record].inTransit = true;
    }
    private void markRecordArrived(int record) {
        stateArray[record].inTransit = false;
    }
    private void markRecordInUse(int record) {
        stateArray[record].inUse = true;
    }
    private void markRecordReleasedFromUse(int record) {
        stateArray[record].inUse = false;
    }
    private void recordAck(int record){
        stateArray[record].ack = true;
    }
    private boolean safeToDeleteRecord(int record){
        RecordState rec = stateArray[record];
        return !rec.inTransit && !rec.inUse && rec.ack;
    }

    private boolean tryAddRecord(RecordType record) {
        if (currentPos >= mModelSize )
            return false;
        // TODO: if durable save to disk

        mRecords.add(currentPos++, record);
        return true;
    }
    void pprint(String s){
        System.out.println("[BaseModel::]>" + s);
    }

    protected Context<RecordType> addRecord(RecordType record) {
        if (!tryAddRecord(record))
            return new Context<>(this, Error.OUT_OF_STORAGE);
        //TODO: race conditions
        if (currentPos == mModelSize) {
            Context<RecordType> ctx = new Context<>(this, Error.OUT_OF_STORAGE);
            eventQueue.enqueue(new ModelEvent((Context<ModelType.RecordType>) ctx, Event.Type.MODEL__NOTIFY_FULL));

        }

        return new Context<>(this, record);
    }

    @Override
    public void record_arrived(RavelPacket pkt, Endpoint endpoint) {
        Context<RecordType> ctx = new Context<>(this);
        RecordType record = unmarshall(pkt.record_data);
        addRecord(record);
        ctx.record = record;
        //notify all subscribers
        notifyArrived(ctx);
    }

    /**
     * Call from the driver layers with a record
     * @param pkt
     * @param endpoint
     */
    @Override
    public void record_departed(RavelPacket pkt, Endpoint endpoint) {
        //TODO: is this an ACK?

        //TODO: is this system packet?
        //normal data
        pprint("record_departed");
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = unmarshall(pkt.record_data);
        //notify all subscribers
        notifyDeparted(ctx);
    }

    @Override
    public void record_saved_durably(RavelPacket pkt) {
        //TODO: only true do remote and durable
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = unmarshall(pkt.record_data);

        //mark saved durably
        //notify all subscribers
        notifySaveDone(ctx);
    }

    @Override
    public void record_saved_endpoint(RavelPacket pkt, Endpoint endpoint) {
        Context<RecordType> ctx = new Context<>(this);
        ctx.record = unmarshall(pkt.record_data);

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

    public Context<RecordType> delete(int deleteField){
        // take a copy for return
        RecordType mDeletedRecord = mRecords.get(deleteField);
        // delete from local array

        for(int i = deleteField ; i < mModelSize ;i++) {
            mRecords.set(i, mRecords.get(i + 1));
        }
        currentPos--;
        return new Context<>(this, mDeletedRecord);
    }

    /***************************************************************/
    /*************** Model Query API implementation ***************/
    /***************************************************************/
    @Override
    public RecordType getFirst() {
        return mRecords.get(0);
    }

    @Override
    public RecordType getLast() {
        return mRecords.get(currentPos);
    }

    @Override
    public RecordType get(int x) {
        return mRecords.get(x);
    }

    @Override
    public Object[] all() {
        return mRecords.toArray();
    }
}
