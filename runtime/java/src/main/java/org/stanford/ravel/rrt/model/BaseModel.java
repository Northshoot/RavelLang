package org.stanford.ravel.rrt.model;

import org.jetbrains.annotations.Nullable;
import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.events.RunnableEvent;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for generated models, containing code to track acks and
 * packets in flight
 *
 * Created by gcampagn on 1/29/17.
 */
public abstract class BaseModel<RecordType extends ModelRecord> implements ModelQuery<RecordType>, ModelBottomAPI, ModelCommandAPI<RecordType> {
    private static class RecordState {
        int expected_acks = 0; //should the record receive ACK
        int in_transit = 0; //Have the record be send?
        int in_save = 0;
        boolean is_valid = false;
        boolean is_arrived = false;
        boolean is_transmit_failed = false;
        Endpoint arrived_from = null;

        void reset() {
            expected_acks = 0;
            in_transit = 0;
            in_save = 0;
            is_valid = false;
            is_arrived = false;
            is_transmit_failed = false;
            arrived_from = null;
        }

        public String toString(){
            return "[expected_acks:" +expected_acks +", in_transit: " + in_transit +", in_save: "+ in_save+
                    ", is_valid: "+is_valid+", : "+is_arrived+ ", is_transmit_failed: "+is_transmit_failed+
                    ", arrived_from: "+arrived_from+ "]";
        }
    }

    // This is accessed by the generated code, so it must be protected
    protected final DispatcherAPI dispatcher;
    //private final RecordState[] state;
    //private final ArrayList<RecordType> mRecords = new ArrayList<>(); //This is array for one flow
    //final ArrayList<RecordType> mValidRecords = new ArrayList<>(); //This is array for one flow
    protected  Map<Integer, ArrayList<RecordType>> mRecordFlowMap = new LinkedHashMap<>(1);
    protected  Map<Integer, ArrayList<RecordType>> mValidRecordsFlowMap = new LinkedHashMap<>(1);
    protected Map<Integer, RecordState[]> mRecordStateMap = new LinkedHashMap<>(4);
    private final boolean mReliable;
    private final boolean mDurable;
    private final int mModelSize;
    final int mModelId;
    //protected int mThisDeviceSrc;

    private int mNextRecordId = 1;

    BaseModel(DispatcherAPI dispatcher, int modelId, int size, boolean reliable, boolean durable) {
        mModelId = modelId;
        mModelSize = size;
        mReliable = reliable;
        mDurable = durable;
        //mRecords.ensureCapacity(size);
        this.dispatcher = dispatcher;
        //mThisDeviceSrc = dispatcher.getDeviceId();
//        state = new RecordState[size];
//        for (int i = 0; i < state.length; i++)
//            state[i] = new RecordState();
    }

    protected void pprint_base(String type, String msg){
        System.out.println("M[ID: " +this.mModelId +", T: "+ type + "]>>>" + msg);

    }
    /**
     * This is replacement for single array structure
     * Now we create an individual flow per source
     * @param src_id source id
     * @return
     */
    protected synchronized ArrayList<RecordType> getRecordFlowMap(int src_id){
        synchronized (mRecordFlowMap) {
            Integer src_key = Integer.valueOf(src_id);
            if (!mRecordFlowMap.containsKey(src_key)) {
                // new node is connected, create flow map
                mRecordFlowMap.put(src_key, new ArrayList<RecordType>(mModelSize));
                getRecordStateMap(src_id);
                getValidRecordsFlowMap(src_id);
            }
        }
        return mRecordFlowMap.get(src_id);
    }

    /**
     * this is replacement for valid records single array structure
     * @param src_id
     * @return
     */
    protected synchronized ArrayList<RecordType>  getValidRecordsFlowMap(int src_id){
            Integer src_key = Integer.valueOf(src_id);
            if (!mValidRecordsFlowMap.containsKey(src_key)) {
                // new node is connected, create flow map
                ArrayList<RecordType> mRecords = new ArrayList<>();
                mRecords.ensureCapacity(mModelSize);
                mValidRecordsFlowMap.put(src_key, mRecords);
            }
        return mValidRecordsFlowMap.get(src_id);
    }

    protected synchronized RecordState[] getRecordStateMap(int src_id){
            Integer src_key = Integer.valueOf(src_id);
            if (!mRecordStateMap.containsKey(src_key)) {
                // new node is connected, create flow map
                RecordState[] state = new RecordState[mModelSize];
                for (int i = 0; i < state.length; i++)
                    state[i] = new RecordState();
                mRecordStateMap.put(src_key, state);
            }
        return mRecordStateMap.get(src_id);
    }

    int getModelSize() {
        return mModelSize;
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
        doSave(record, pkt.getSource(),false);
    }

    boolean  markSaved(int src, int recordPos) {
        boolean ret;
        synchronized ( mRecordStateMap ) {
            RecordState[] state = getRecordStateMap(src);
            assert state[recordPos].in_save > 0;
//     try {
//        pprint_base("Base", "markSaved src: " + src + " recordPos " + recordPos + " state.lenght " + state.length);
//
//        } catch (Exception e){
//            //FIXME: why do we end up here?
//            System.err.println("Index out of range: got " + recordPos + " size: " + state.length);
//            return true;
//        }
            pprint_base("Base", "markSaved src: " + src + " recordPos " + recordPos + " state.lenght " + state.length);
            state[recordPos].in_save--;
            ret = state[recordPos].in_save == 0;
        }
        return ret;
    }
    void markInSave(int src, int recordPos) {
        mRecordStateMap.get(src)[recordPos].in_save++;
    }

    boolean isArrived(int src, int recordPos) {
        return mRecordStateMap.get(src)[recordPos].is_arrived;
    }
    Endpoint getArrivedFrom(int src, int recordPos) {
        return mRecordStateMap.get(src)[recordPos].arrived_from;
    }
    void markArrived(int src, int recordPos, boolean is_arrived, Endpoint from) {
        RecordState [] state = mRecordStateMap.get(src);
        state[recordPos].is_arrived = is_arrived;
        state[recordPos].arrived_from = from;
    }
    boolean isValid(int src, int recordPos) {
        try {
            return mRecordStateMap.get(src)[recordPos].is_valid;
        } catch ( NullPointerException e){
            //record has been deleted
            return false;
        }
    }
    boolean markNotInTransit(int src, int recordPos) {
        RecordState[] state = mRecordStateMap.get(src);
        try {
            state[recordPos].in_transit--;
            return state[recordPos].in_transit == 0;
        } catch (ArrayIndexOutOfBoundsException e){
            //we end up here when the record has been deleted but send done arrived
            return true;
        }

    }
    boolean isTransmitFailed(int src, int recordPos) {
        return mRecordStateMap.get(src)[recordPos].is_transmit_failed;
    }
    boolean isInTransit(int src, int recordPos) {
        return mRecordStateMap.get(src)[recordPos].in_transit > 0;
    }
    void markTransmitNotFailed(int src, int recordPos) {
        mRecordStateMap.get(src)[recordPos].is_transmit_failed = false;
    }

    //TODO: need to be fixed properluy
    private void queueFullEvent() {
        dispatcher.queueEvent(new RunnableEvent() {
            @Override
            public void run() {
                Context<RecordType> ctx = new Context<>(BaseModel.this, Error.OUT_OF_STORAGE);
                notifyFull(ctx);
            }
        });
    }
    void queueSaveDone(final RecordType record, final int src) {
//        try {
            dispatcher.queueEvent(new RunnableEvent() {
                @Override
                public void run() {
                    Context<RecordType> ctx = new Context<>(BaseModel.this, record);
                    int recordPos = recordPosFromRecord(record, src);
                    assert recordPos >= 0;
                    markSaved(src, recordPos);

                    if (isValid(src, recordPos))
                        notifySaveDone(ctx);
                    else
                        freeRecord(src, recordPos);
                }
            });
//        } catch (Exception e ){
//            //FIXME: we just catch them all
//            System.err.println("Index out of range: got " + record.toString());
//        }
    }

    /**
     * Returns index of the recodd sored in record flow map
     * or -1 id record is not found
     * @param record
     * @param src flow source
     * @return record possition of -1
     */
    int recordPosFromRecord(RecordType record, int src) {
//        pprint_base("Base",  " src " + src);
        ArrayList<RecordType> rt = getRecordFlowMap(src);
        return rt.indexOf(record);
    }

    int findRecordWithId(int src, int recordId) {
        ArrayList<RecordType> mRecords = getRecordFlowMap(src);
        for (int i = 0; i < mRecords.size(); i++) {
            RecordType record = mRecords.get(i);
            if (record == null)
                continue;
            if (record.index() == recordId)
                return i;
        }
        return -1;
    }
    RecordType recordAt(int src,int recordPos) {
        ArrayList<RecordType> mRecords = getRecordFlowMap(src);
        if (mRecords.size() <= recordPos)
            return null;
        else
            return mRecords.get(recordPos);
    }
    protected void assignIndex(RecordType record) {
        //record index is 16b need to overflow pretty
        //TODO: add support for index mapping and overfloww counting from embedded devices
        if( mNextRecordId == 32767) {
            //we reset index
            mNextRecordId = -1;
        }
        record.index(mNextRecordId++);
    }

    private int tryAddRecord(int src, RecordType record) {
        ArrayList<RecordType> mRecords = getRecordFlowMap(src);
        if (mRecords.size() == mModelSize){
            return -1;
        }
        for (int i = 0; i < mRecords.size(); i++) {
            if (mRecords.get(i) == null) {
                mRecords.set(i, record);
                return i;
            }
        }
        mRecords.add(record);
        return mRecords.size()-1;
    }

     Context<RecordType>  doSave(RecordType record, int src, boolean saveDurably) {
        int recordPos = recordPosFromRecord(record, src);
        RecordState[] state = getRecordStateMap(src);
        if (recordPos < 0) {
            recordPos = tryAddRecord(src, record);
            if (recordPos < 0) {
                return new Context<>(this, Error.OUT_OF_STORAGE);
            }
            if (recordFlowSize(src) == mModelSize)
                queueFullEvent();
        }

        if (!state[recordPos].is_valid) {
            state[recordPos].is_valid = true;
            getValidRecordsFlowMap(src).add(record);
        }

        if (!saveDurably)
            return new Context<>(this, record);

        state[recordPos].in_save++;

        if (isDurable()) {
            RavelPacket pkt;

            pkt = RavelPacket.fromRecord(marshall(record, null), dispatcher.getDeviceId());
            dispatcher.model__saveDurably(pkt);
            return new Context<>(this, Error.IN_TRANSIT);
        } else {
            return new Context<>(this, record);
        }
    }

    private int recordFlowSize(int src) {
        return getRecordFlowMap(src).size();
    }

    void freeRecord(int src, int recordPos) {
        getRecordStateMap(src)[recordPos].reset();
    }

    boolean doDelete(RecordType record, int src) {
        int recordPos = recordPosFromRecord(record, src);

        if (recordPos < 0 || !getRecordStateMap(src)[recordPos].is_valid) {
            return false;
        } else {
            getRecordStateMap(src)[recordPos].is_valid = false;
            getValidRecordsFlowMap(src).remove(record);
            if (getRecordStateMap(src)[recordPos].in_save > 0 || getRecordStateMap(src)[recordPos].in_transit > 0 ||
                    getRecordStateMap(src)[recordPos].expected_acks > 0) {
                // we cannot delete right away
                return true;
            } else {
                freeRecord(src, recordPos);
                return true;
            }
        }
    }

    Error sendOneRecord(int recordPos, int src, RecordType record, Endpoint e, boolean markAsInSave) {
        RavelPacket pkt = RavelPacket.fromRecord(marshall(record, e), dispatcher.getDeviceId());
        pkt.setDestination(dispatcher.getAppId(), e.getId());
        if (isReliable())
            getRecordStateMap(src)[recordPos].expected_acks ++;
        if (markAsInSave)
            getRecordStateMap(src)[recordPos].in_save ++;
        //TODO: fix this properly
        return dispatcher.model__sendData(pkt, e);
//        if (e.isConnected()) {
//            System.out.println("CONNECTED " + e.toString());
//            return dispatcher.model__sendData(pkt, e);
//        } else {
//             System.out.println("NOT CONNECTED " + e.toString());
//            return Error.ENDPOINT_UNREACHABLE;}
    }

    synchronized Error sendRecord(int recordPos, int src, RecordType record, Collection<Integer> endpointNames, boolean markAsInSave) {
        Collection<Endpoint> endpoints = new ArrayList<>();
        for (int name : endpointNames)
            endpoints.addAll(dispatcher.getEndpointsByName(name));

        if (endpoints.isEmpty()) {
            getRecordStateMap(src)[recordPos].is_transmit_failed = true;
            return Error.SUCCESS;
        }

        Error error = Error.SUCCESS;
        for (Endpoint e : endpoints) {
            try {
                Error error2 = sendOneRecord(recordPos, src, record, e, markAsInSave);
                if (error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS)
                    getRecordStateMap(src)[recordPos].is_transmit_failed = true;
                else {
                    System.out.println("rec src: " + src + "Record pos: " + recordPos + " Record state size " + getRecordStateMap(src).length);
                    getRecordStateMap(src)[recordPos].in_transit++;
                }
                if ((error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS) || error == Error.SUCCESS)
                    error = error2;
            } catch (SecurityException securityException) {
                if (error == Error.SUCCESS)
                    error = Error.SECURITY_ERROR;
            } catch (NullPointerException ex){
                System.out.println("Exception: recordpos: " + recordPos + " size" + getRecordStateMap(src).length);
            }
        }
        return error;
    }

    RecordType savedDurably(RavelPacket pkt) {
        int src = pkt.getSource();
        int recordPos = findRecordWithId(src,pkt.record_id);
        if (recordPos < 0)
            return null;
        RecordType record = getRecord(src, recordPos);

        markSaved(src, recordPos);

        if (getRecordStateMap(src)[recordPos].is_valid) {
            return record;
        } else {
            freeRecord(src, recordPos);
            return null;
        }
    }

    private RecordType getRecord(int source, int recordPos) {
        return  getRecordFlowMap(source).get(recordPos);
    }

    boolean handleAck(int src, int recordPos) {
        getRecordStateMap(src)[recordPos].expected_acks--;
        return getRecordStateMap(src)[recordPos].expected_acks == 0;
    }

    Context<RecordType> handleRecord(RecordType record, RavelPacket pkt, Endpoint endpoint) {
        try {
            record = unmarshall(record, pkt.getRecordData(), endpoint);
            return doSave( record, pkt.getSource(),true);
        } catch(SecurityException e) {
            return new Context<>(this, Error.SECURITY_ERROR);
        }
    }

    /*
    TODO: handle this for multiple streams for replicated model
     */
    Error forwardPacket(RavelPacket pkt, int dst, RecordType record) {
//        Collection<Endpoint> endpoints = new ArrayList<>();
//        System.out.println("forwardPacket: " + endpointNames.toString());
//        int src = pkt.getSource();
//        for (int name : endpointNames)
//            endpoints.addAll(dispatcher.getEndpointsByName(name));
        Collection<Endpoint> endpoints= dispatcher.getEndpointsByName(dst);
        int recordPos = -1;
        int src = pkt.getSource();
        if (record != null)
            recordPos = recordPosFromRecord(record, src);

        Error error = Error.SUCCESS;
      for (Endpoint e : endpoints) {
           if (recordPos >= 0) {
               if (isReliable())
                    getRecordStateMap(src)[recordPos].expected_acks++;
            }
            Error error2;
            //pkt.setDestination(dispatcher.getAppId(), dst);
            error2 = dispatcher.model__sendData(pkt, e);
            // TODO: this this properly
//            if (e.isConnected())
//                error2 = dispatcher.model__sendData(pkt, e);
//            else
//                error2 = Error.ENDPOINT_UNREACHABLE;
            if (recordPos >= 0) {
                if (error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS)
                    getRecordStateMap(src)[recordPos].is_transmit_failed = true;
                else
                    getRecordStateMap(src)[recordPos].in_transit++;
            }
            if ((error2 != Error.IN_TRANSIT && error2 != Error.SUCCESS) || error == Error.SUCCESS)
                error = error2;
        }
        return error;
    }

    /***************************************************************/
    /*************** Model Query API implementation ***************/
    /***************************************************************/
    /**
     * TODO: this needs to be redefined and addressed
     * @return
     */
    @Override
    public RecordType first(Endpoint endpoint) {
        int  flow_id = -1;
        if (endpoint != null){
            flow_id = endpoint.getId();
        }
        else{
            flow_id = dispatcher.getDeviceId();
        }
        //Handle deleted records
        System.out.println("FLow id: " + flow_id + " valid records: " + getValidRecordsFlowMap(flow_id).size()
                            + " flow records " + getRecordFlowMap(flow_id).size());
        if(getValidRecordsFlowMap(flow_id).size()>0 )
            return getValidRecordsFlowMap(flow_id).get(0);
        else
            return null;

    }

    @Override
    public RecordType last() {
        return getValidRecordsFlowMap(dispatcher.getDeviceId()).get(getValidRecordsFlowMap(dispatcher.getDeviceId()).size()-1);
    }

    @Override
    public RecordType get(int x) {
        return getValidRecordsFlowMap(dispatcher.getDeviceId()).get(x);
    }

    @Override
    public RecordType[] all(RecordType[] unused) {
        return getValidRecordsFlowMap(dispatcher.getDeviceId()).toArray(unused);
    }

    @Override
    public Iterator<RecordType> iterator() {
        return getValidRecordsFlowMap(dispatcher.getDeviceId()).iterator();
    }

    @Override
    public void clear(@Nullable Endpoint endpoint) {
        if(endpoint != null){
            int src = endpoint.getId();
            mRecordFlowMap.remove(src);
            mValidRecordsFlowMap.remove(src);

           // mRecordStateMap.remove(src);
        } else {
            mRecordFlowMap.clear();
            mValidRecordsFlowMap.clear();
           // mRecordStateMap.clear();
        }

    }

    @Override
    public synchronized void clearAll() {
        mRecordFlowMap.clear();
        mValidRecordsFlowMap.clear();
        mRecordStateMap.clear();
    }

    @Override
    public int size(@Nullable Endpoint endpoint) {
        if(endpoint != null){
//            System.out.println("getValidRecordsFlowMap(endpoint.getId()).size() " + getValidRecordsFlowMap(endpoint.getId()).size()
//                    + " getRecordFlowMap(endpoint.getId()).size() " + getRecordFlowMap(endpoint.getId()).size());
            return getValidRecordsFlowMap(endpoint.getId()).size();
        }
        return getValidRecordsFlowMap(dispatcher.getDeviceId()).size();
    }
}
