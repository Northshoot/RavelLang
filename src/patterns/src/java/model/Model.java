//AUTOGEN packet
package patterns.src.java.model;

//AUTOGEN imports
import patterns.src.java.rrt.AppDispatcher;
import patterns.src.java.rrt.RavelPacket;
import patterns.src.java.tiers.Endpoint;
import patterns.src.java.tiers.Error;
import patterns.src.java.controller.ModelController;
import patterns.src.java.controller.ModelCtrl;
import patterns.src.java.rrt.Context;
import patterns.src.java.utils.ByteWork;

//Standard utilities
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lauril on 1/23/14.
 */
public class Model implements ModelCommandAPI, ModelQuery, ModelBottomAPI{


    /**
     * Record is stored
     */
    private Record mRecords[];

    /**
     * Controller Listeners
     */
    private Map<String, ModelCtrl> mModelListeners;
    private AppDispatcher mAppDispacher;

    private int current_pos = 0;

    //AUTOGEN add type
    private ModelType mType = ModelType.STREAMING;

    //AUTOGEN properties
    private int mSize = 10;
    private boolean reliable = false;
    private boolean durable = false;


    //AUTOGEN endpoints
    private Endpoint mEndpoint;

    //AUTOGEN ravel enumerates all models
    public final static int MODEL_ID = 5;
   
 
    public final static int IN_REST = 1;
    public final static int IN_TRANSIT = 2;
    public final static int IN_USE = 3;
    public final static int ACK = 4;

    //AUTOGEN ACK table
    //NOTE arrays in java are initialized to default value, here false
    boolean[] record_0_row = new boolean[4];
    boolean[] record_1_row = new boolean[4];
    boolean[] record_2_row = new boolean[4];
    boolean[] record_3_row = new boolean[4];
    boolean[] record_4_row = new boolean[4];
    boolean[] record_5_row = new boolean[4];
    boolean[] record_6_row = new boolean[4];
    boolean[] record_7_row = new boolean[4];
    boolean[] record_8_row = new boolean[4];
    boolean[] record_9_row = new boolean[4];

    
    private void record_IN_REST(int record){
        getRecordTable(record)[IN_REST] = true;
    }

    private void record_sent(int record){
        getRecordTable(record)[IN_TRANSIT] = true;
    }

    private void record_arrived(int record){
        getRecordTable(record)[IN_TRANSIT] = false;
    }
    private void record_in_use(int record){
        getRecordTable(record)[IN_USE] = true;
    }

    private void record_released_from_use(int record){
        getRecordTable(record)[IN_USE] = false;
    }

    private void record_ack(int record){
        getRecordTable(record)[ACK] = true;
    }

    private boolean safe_delete(int record){
        boolean[] rec = getRecordTable(record);
        return !rec[IN_TRANSIT] && !rec[IN_USE] && rec[ACK];
    }

    private boolean[] getRecordTable(int record){
        switch (record){
            case 0:
                return record_0_row;
            case 1:
                return record_1_row;
            case 2:
                return record_2_row;
            case 3:
                return record_3_row;
            case 4:
                return record_4_row;
            case 5:
                return record_5_row;
            case 6:
                return record_6_row;
            case 7:
                return record_7_row;
            case 8:
                return record_8_row;
            case 9:
                return record_9_row;

        }
        return null;
    }


    public Model(AppDispatcher appDispatcher){
        this.mAppDispacher = appDispatcher;
        this.mModelListeners = new HashMap<>();
        this.mRecords = new Record[mSize];
    }

    /*************** Model internal functionality  ***************/


    private void addRecord(Record r){
        //if durable save to disk
        mRecords[current_pos++]=r;
    }

    public void setController(ModelController controller) {
        mModelListeners.put(controller.name, controller);
    }

    /***************************************************************/
    /*************** Model Bottom API implementation ***************/
    /***************************************************************/

    @Override
    public void record_arrived(RavelPacket pkt, Endpoint endpoint) {
        Context ctx = new Context(this);
        Record record = new Record(pkt.record_data);
        System.out.println("RX record");
        addRecord(record);
        ctx.mError = Error.SUCCESS;
        ctx.mRecord = record;
        //notify all subscribers
        mModelListeners.forEach((k,v) -> v.arrived(ctx));

    }

    @Override
    public void record_departed(Model.Record record, Endpoint endpoint) {
        Context ctx = new Context(this);
        ctx.mError = Error.SUCCESS;
        ctx.mRecord = record;
        //notify all subscribers
        mModelListeners.forEach((k,v) -> v.departed(ctx));
    }

    @Override
    public void record_saved_durably(Model.Record record) {
        Context ctx = new Context(this);
        ctx.mError = Error.SUCCESS;
        ctx.mRecord = record;
        //mark saved durably
        //notify all subscribers
        mModelListeners.forEach((k,v) -> v.save_done(ctx));
    }

    @Override
    public void record_saved_endpoint(Model.Record record, Endpoint endpoint) {
        Context ctx = new Context(this);
        ctx.mError = Error.SUCCESS;
        ctx.mRecord = record;
        //notify all subscribers
        mModelListeners.forEach((k,v) -> v.save_done(ctx));
    }

    @Override
    public void endpoint_connected(Endpoint endpoint){

    }
    /***************************************************************/
    /*************** Model Command API implementation ***************/
    /***************************************************************/

    public Model.Record create(){
        //contract: if current not last
        //return empty record
        if(current_pos < mSize){
            return new Record();
        }
        return null;
    }

    public Context save(Context ctx){
        //AUTOGEN code will depend on the type
        //local can not be reliable!
        if(current_pos >= mSize){
            ctx.mError = Error.OUT_OF_STORAGE;
            return ctx;
        } else {
            addRecord(ctx.mRecord);
            if (mType != ModelType.LOCAL) { //TODO: remove in autogen
                if (mEndpoint.isConnected()) {
                    mAppDispacher.send_data(ctx.mRecord, mEndpoint);
                    ctx.mError = Error.IN_TRANSIT;
                } else {
                    ctx.mError = Error.WAITING_FOR_NETWORK;
                }
                return ctx;
            } //end check for local
        }
        return null;
    }

    public Context delete(int deleteField){
        //take a copy for return
        Record mDeletedRecord = mRecords[deleteField];
        //delete from local array
        for(int i=deleteField ; i< mSize ;i++) {
            mRecords[i] = mRecords[i + 1];
            mRecords[i].position=i+1;
        }
        current_pos--;
        return new Context(this, mDeletedRecord, Error.SUCCESS);
    }

    /***************************************************************/
    /*************** Model Query API implementation ***************/
    /***************************************************************/
    @Override
    public Record getFirst() {
        return mRecords[0];
    }

    @Override
    public Record getLast() {
        return mRecords[current_pos];
    }

    @Override
    public Record get(int x) {
        if( x >= 0 && x <= current_pos) {
            return mRecords[x];
        } else {
            return getFirst();
        }
    }

    @Override
    public Model.Record[] all() {
        return mRecords;
    }

    public class Record implements Serializable {
        int position;
        int state;
        int field1;
        int field2;
        int field3;
        int field4;

        public Record(int position,
                      int field1_val,
                      int field2_val,
                      int field3_val,
                      int field4_val){

            this.position = position;
            this.field1 = field1_val;
            this.field2 = field2_val;
            this.field3 = field3_val;
            this.field4 = field4_val;
        }

        public Record(byte[] data){
            //make record out of byte[]
            //AUTOGEN
            this.field1 = ByteWork.convertFourUnsignedBytesToInt(
                    ByteWork.getBytes(data,0,4)
            );
            this.field2 = ByteWork.convertFourUnsignedBytesToInt(
                    ByteWork.getBytes(data,4,8)
            );
            this.field3 = ByteWork.convertFourUnsignedBytesToInt(
                    ByteWork.getBytes(data,8,12)
            );
            this.field4 = ByteWork.convertFourUnsignedBytesToInt(
                    ByteWork.getBytes(data,12,16)
            );
        }

        public Record() {

        }
    }

}
