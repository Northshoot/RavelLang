package org.stanford.ravel.rrt.model;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.events.RunnableEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.*;

/**
 * Base class for generated models, containing code to track acks and
 * packets in flight
 *
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseModel<RecordType extends ModelRecord> implements ModelQuery<RecordType>, ModelBottomAPI, ModelCommandAPI<RecordType> {
    private static class RecordState {
        int expected_acks = 0;
        int in_transit = 0;
        int in_save = 0;
        boolean is_valid = false;
        boolean is_arrived = false;

        void reset() {
            expected_acks = 0;
            in_transit = 0;
            in_save = 0;
            is_valid = false;
            is_arrived = false;
        }
    }

    // This is accessed by the generated code, so it must be protected
    protected final DispatcherAPI dispatcher;
    private final RecordState[] state;
    private final ArrayList<RecordType> mRecords = new ArrayList<>();
    private final ArrayList<RecordType> mValidRecords = new ArrayList<>();
    private final boolean mReliable;
    private final boolean mDurable;
    private final int mModelSize;

    private int mNextRecordId = 1;

    BaseModel(DispatcherAPI dispatcher, int size, boolean reliable, boolean durable) {
        mModelSize = size;
        mReliable = reliable;
        mDurable = durable;
        mRecords.ensureCapacity(size);
        this.dispatcher = dispatcher;

        state = new RecordState[size];
        for (int i = 0; i < state.length; i++)
            state[i] = new RecordState();
    }

    final boolean isReliable() {
        return mReliable;
    }

    final boolean isDurable() {
        return mDurable;
    }

    // the generated methods for dispatching events
    protected abstract void notifyFull(Context<RecordType> ctx);
    protected abstract void notifyArrived(Context<RecordType> ctx);
    protected abstract void notifyDeparted(Context<RecordType> ctx);
    protected abstract void notifySaveDone(Context<RecordType> ctx);

    // the generated methods for marshalling/unmarshalling
    protected abstract RecordType create();
    protected abstract byte[] marshall(RecordType record, Endpoint endpoint);
    protected abstract RecordType unmarshall(RecordType record, byte[] data, Endpoint endpoint);

    @Override
    public void recordLoaded(RavelPacket pkt) {
        RecordType record = create();
        record = unmarshall(record, pkt.getRecordData(), null);
        doSave(record, false);
    }

    void markSaved(int recordPos) {
        assert state[recordPos].in_save > 0;
        state[recordPos].in_save--;
    }
    boolean isArrived(int recordPos) {
        return state[recordPos].is_arrived;
    }
    void markArrived(int recordPos, boolean is_arrived) {
        state[recordPos].is_arrived = is_arrived;
    }
    boolean isValid(int recordPos) {
        return state[recordPos].is_valid;
    }
    boolean markNotInTransit(int recordPos) {
        state[recordPos].in_transit --;
        return state[recordPos].in_transit == 0;
    }

    private void queueFullEvent() {
        dispatcher.queueEvent(new RunnableEvent() {
            @Override
            public void run() {
                Context<RecordType> ctx = new Context<>(BaseModel.this, Error.OUT_OF_STORAGE);
                notifyFull(ctx);
            }
        });
    }
    void queueSaveDone(final RecordType record) {
        dispatcher.queueEvent(new RunnableEvent() {
            @Override
            public void run() {
                Context<RecordType> ctx = new Context<>(BaseModel.this, record);
                int recordPos = recordPosFromRecord(record);
                assert recordPos >= 0;
                markSaved(recordPos);

                if (isValid(recordPos))
                    notifySaveDone(ctx);
                else
                    freeRecord(recordPos);
            }
        });
    }

    int recordPosFromRecord(RecordType record) {
        return mRecords.indexOf(record);
    }
    int findRecordWithId(int recordId) {
        for (int i = 0; i < mRecords.size(); i++) {
            RecordType record = mRecords.get(i);
            if (record == null)
                continue;
            if (record.index() == recordId)
                return i;
        }
        return -1;
    }
    RecordType recordAt(int recordPos) {
        return mRecords.get(recordPos);
    }

    private int tryAddRecord(RecordType record) {
        for (int i = 0; i < mRecords.size(); i++) {
            if (mRecords.get(i) == null) {
                mRecords.set(i, record);
                return i;
            }
        }
        if (mRecords.size() == mModelSize)
            return -1;
        mRecords.add(record);
        return mRecords.size()-1;
    }

    Context<RecordType> doSave(RecordType record, boolean saveDurably) {
        int recordPos = recordPosFromRecord(record);

        if (recordPos < 0) {
            recordPos = tryAddRecord(record);
            if (recordPos < 0)
                return new Context<>(this, Error.OUT_OF_STORAGE);

            if (mRecords.size() == mModelSize)
                queueFullEvent();
        }

        if (!state[recordPos].is_valid) {
            record.index(mNextRecordId++);
            state[recordPos].is_valid = true;
            mValidRecords.add(record);
        }

        if (!saveDurably)
            return new Context<>(this, record);

        state[recordPos].in_save++;

        if (isDurable()) {
            RavelPacket pkt;

            pkt = RavelPacket.fromRecord(marshall(record, null));
            dispatcher.model__saveDurably(pkt);
            return new Context<>(this, Error.IN_TRANSIT);
        } else {
            return new Context<>(this, record);
        }
    }

    void freeRecord(int recordPos) {
        mRecords.set(recordPos, null);
        state[recordPos].reset();
    }

    boolean doDelete(RecordType record) {
        int recordPos = recordPosFromRecord(record);

        if (recordPos < 0 || !state[recordPos].is_valid) {
            return false;
        } else {
            state[recordPos].is_valid = false;
            mValidRecords.remove(record);
            if (state[recordPos].in_save > 0 || state[recordPos].in_transit > 0 ||
                    state[recordPos].expected_acks > 0) {
                // we cannot delete right away
                return true;
            } else {
                freeRecord(recordPos);
                return true;
            }
        }
    }

    Error sendOneRecord(int recordPos, RecordType record, Endpoint e) {
        RavelPacket pkt = RavelPacket.fromRecord(marshall(record, e));

        state[recordPos].in_transit ++;
        if (isReliable())
            state[recordPos].expected_acks ++;

        return dispatcher.model__sendData(pkt, e);
    }

    Error sendRecord(int recordPos, RecordType record, Collection<Integer> endpointNames) {
        Collection<Endpoint> endpoints = new ArrayList<>();
        for (int name : endpointNames)
            endpoints.addAll(dispatcher.getEndpointsByName(name));

        Error error = Error.SUCCESS;
        for (Endpoint e : endpoints) {
            try {
                Error error2 = sendOneRecord(recordPos, record, e);
                if ((error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS) || error == Error.SUCCESS)
                    error = error2;
            } catch(SecurityException securityException) {
                if (error == Error.SUCCESS)
                    error = Error.SECURITY_ERROR;
            }
        }
        return error;
    }

    RecordType savedDurably(RavelPacket pkt) {
        int recordPos = findRecordWithId(pkt.record_id);
        if (recordPos < 0)
            return null;
        RecordType record = mRecords.get(recordPos);

        markSaved(recordPos);

        if (state[recordPos].is_valid) {
            return record;
        } else {
            freeRecord(recordPos);
            return null;
        }
    }

    boolean handleAck(int recordPos) {
        state[recordPos].expected_acks--;
        return state[recordPos].expected_acks == 0;
    }

    Context<RecordType> handleRecord(RecordType record, RavelPacket pkt, Endpoint endpoint) {
        try {
            record = unmarshall(record, pkt.getRecordData(), endpoint);
            return doSave(record, true);
        } catch(SecurityException e) {
            return new Context<>(this, Error.SECURITY_ERROR);
        }
    }

    Error forwardPacket(RavelPacket pkt, Collection<Integer> endpointNames, RecordType record) {
        Collection<Endpoint> endpoints = new ArrayList<>();
        for (int name : endpointNames)
            endpoints.addAll(dispatcher.getEndpointsByName(name));

        int recordPos = -1;
        if (record != null)
            recordPos = recordPosFromRecord(record);

        Error error = Error.SUCCESS;
        for (Endpoint e : endpoints) {
            if (recordPos >= 0) {
                state[recordPos].in_transit++;

                if (isReliable())
                    state[recordPos].expected_acks++;
            }

            Error error2 = dispatcher.model__sendData(pkt, e);
            if ((error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS) || error == Error.SUCCESS)
                error = error2;
        }
        return error;
    }

    /***************************************************************/
    /*************** Model Query API implementation ***************/
    /***************************************************************/
    @Override
    public RecordType first() {
        return mValidRecords.get(0);
    }

    @Override
    public RecordType last() {
        return mValidRecords.get(mValidRecords.size()-1);
    }

    @Override
    public RecordType get(int x) {
        return mValidRecords.get(x);
    }

    @Override
    public RecordType[] all(RecordType[] unused) {
        return mValidRecords.toArray(unused);
    }

    @Override
    public Iterator<RecordType> iterator() {
        return mValidRecords.iterator();
    }

    @Override
    public void clear() {
        mRecords.clear();
        mValidRecords.clear();
    }

    @Override
    public int size() {
        return mValidRecords.size();
    }
}
