//AUTOGEN packet
package patterns.src.java.model;

//AUTOGEN imports
import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.model.StreamingModel;
import org.stanford.ravel.rrt.utils.ByteWork;
import patterns.src.java.app.AppDispatcher;
import patterns.src.java.controller.ModelController;

//Standard utilities
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lauril on 1/23/14.
 */
public class Model extends StreamingModel<Model.Record>{
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
    protected int getModelID() {
        return MODEL_ID;
    }

    @Override
    protected Record unmarshall(byte[] data) {
        return new Record(data);
    }

    public static Record create() {
        return new Record();
    }

    public static class Record implements Serializable, ModelRecord {
        int model_id = Model.MODEL_ID;
        int idx;
        public int field1;
        public int field2;
        public int field3;
        public int field4;

        public Record(int position,
                      int field1_val,
                      int field2_val,
                      int field3_val,
                      int field4_val){

            this.idx = position;
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

        public byte[] toBytes(){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            //AUTOGEN: write to
            outputStream.write(model_id);
            outputStream.write(idx);
            outputStream.write(field1);
            outputStream.write(field2);
            outputStream.write(field3);
            outputStream.write(field4);
            System.err.println("toBytes: " + outputStream.toByteArray().length);
            //AUTOGEN END
            return outputStream.toByteArray();
        }
    }

}
