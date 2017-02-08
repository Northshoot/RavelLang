//AUTOGEN packet
package patterns.src.java.model;

//AUTOGEN imports
import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.model.StreamingModel;
import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.utils.GrowableByteArray;
import patterns.src.java.app.AppDispatcher;
import patterns.src.java.controller.ModelController;

import java.io.Serializable;

//Standard utilities

/**
 * Created by lauril on 1/23/14.
 */
public class Model extends StreamingModel<Model.Record> {
    /**
     * Controller Listeners
     */
    //AUTOGEN: controller naming
    private ModelController mcntr;


    public void setModelController(ModelController mc){
        this.mcntr = mc;
    }

    //AUTOGEN add type

    //AUTOGEN properties
    private static final int MODEL_SIZE = 10;

    //AUTOGEN ravel enumerates all models
    public final static int MODEL_ID = 5;
    //AUTOGEN: schema size
    public final static int RECORD_SIZE = 6*4;

    public Model(AppDispatcher appDispatcher) {
        super(appDispatcher, MODEL_SIZE);
    }

    void pprint(String s){
        System.out.println("[StreamingModel::Model]>" + s);
    }

    /*************** Model internal functionality  ***************/

    @Override
    protected void notifyFull(org.stanford.ravel.rrt.Context<Record> ctx) {
        pprint("notifyFull");
        //AUTOGEN: list of subscribing controllers
        mcntr.Model_full(ctx);
    }

    @Override
    protected void notifyArrived(org.stanford.ravel.rrt.Context<Record> ctx) {
        pprint("notifyArrived");

        /* FIXME
        //TODO: this is fast hack
        if(super.mEndpointUpp != null){
            pprint("resending");
            Error error = null;
            RavelPacket ravelPacket = RavelPacket.fromRecord(ctx.record.toBytes());
            mDispatcher.model__sendData(ravelPacket, mEndpointUpp);
            //TODO: deal with forward error
        }
        */
        //AUTOGEN: list of subscribing controllers

        mcntr.Model_arrived(ctx);

    }

    @Override
    protected void notifyDeparted(org.stanford.ravel.rrt.Context<Record> ctx) {
        pprint("notifyDeparted");
        //AUTOGEN: list of subscribing controllers
        mcntr.Model_departed(ctx);

    }

    @Override
    protected void notifySaveDone(org.stanford.ravel.rrt.Context<Record> ctx) {
        pprint("notifySaveDone");
        //AUTOGEN: list of subscribing controllers
        mcntr.Model_save_done(ctx);

    }


    @Override
    protected Record unmarshall(byte[] data) {
        return new Record(data);
    }

    public static Record create() {
        return new Record();
    }

    public static class Record implements Serializable, ModelRecord {
        protected int model_id = Model.MODEL_ID;
        protected int idx;
        public int field1;
        public int field2;
        public int field3;
        public int field4;

        public Record(byte[] data){
            //make record out of byte[]
            //AUTOGEN

            this.model_id = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data,0,4)
            );

            this.idx = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data,4,8)
            );
            this.field1 = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data,8,12)
            );
            this.field2 = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data,12,16)
            );
            this.field3 = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data,16,20)
            );
            this.field4 = ByteWork.convertFourBytesToInt(
                    ByteWork.getBytes(data, 20, 24)
            );
        }

        public Record() {

        }

        public byte[] toBytes() {
            GrowableByteArray outputStream = new GrowableByteArray();

            //AUTOGEN: write to
            outputStream.write(ByteWork.getByteArray(model_id));
            outputStream.write(ByteWork.getByteArray(idx));
            outputStream.write(ByteWork.getByteArray(field1));
            outputStream.write(ByteWork.getByteArray(field2));
            outputStream.write(ByteWork.getByteArray(field3));
            outputStream.write(ByteWork.getByteArray(field4));
            //AUTOGEN END

            return outputStream.toByteArray();
        }

        @Override
        public void index(int i) {
            this.idx = i;
        }
    }

}
