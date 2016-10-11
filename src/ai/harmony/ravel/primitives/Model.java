package ai.harmony.ravel.primitives;

import ai.harmony.ravel.primitives.Fields.Field;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends ParametrizedComponent {

    private static Logger LOGGER = Logger.getLogger(Model.class.getName());
    private String mComment = "Default comments";




    public enum Type { LOCAL, STREAMING, REPLICATED, tINVALID }

    Model.Type mModelType;
    Controller mController = null;

    private Map<String, Field> mFields = new LinkedHashMap<>();
    private Map<String, Variable> mPropertiesMap = new LinkedHashMap<>();

    /**
     * the provided api
     */
    private String mArrived="arrived";
    private String mDeparted="departed";
    private String mCreate="create";
    private String mSaveDone="save";
    private String mBufferSaveDone="bufferSave";
    private String mFull="full";
    private String mDelete="delete";
    private String mGet="get";
    private String mFirst="first";
    private String mLast="last";
    private String mDestroy="destroy";
    private Map<String, String> mEvents;

    //TODO: implement builder pattern
    public Model(String name, Model.Type t){
        super(name,name+"Model");
        mEvents = new LinkedHashMap<>();
        //TODO: implement real error handling
        if (t == Type.tINVALID ) {
            LOGGER.severe("Invalid model type! ");
        }
        this.mModelType = t;
    }

    public String getModelName(){
        return mName;
    }
    /**
     * Model API functions bellow
     */

    //internal to system

    //get buffer initialization
    public String getInitFunction(){
        return getCName()+"__innit";
    }

    public List<String> getEvents(){
        List<String> lst = new ArrayList<>();
        lst.addAll(mEvents.values());
        return lst;
    }
    //destroy buffer
    public String getDestroyFunction(){
        return getCName() +"__destroy";
    }
    //create
    public String getCreateFunction(){
        return getCName() + "__create";
    }

    //save
    public String getSaveFunction(){
        return getCName()+"__save";
    }


    //delete
    public String getDeleteFunction(){
        return getCName()+"__delete";
    }
    //queries
    //get with position id
    public String getRecordPosition(){
        return getCName()+"__get";
    }

    //get first
    public String getFirstRecord(){
        return getCName()+"__get_first";
    }
    //get last
    public String getLastRecord(){
        return getCName()+"__get_last";
    }
    /**
     * Model Events
     * we add controller name to distinguish the subscribers
     *
     */
    //record arrived to the tier
    public String getArrivedEvent(){
        return mEvents.get(mArrived);
    }

    //record departed from the tier
    public String getDepartedEvent(){
        return mEvents.get(mDeparted);
    }
    //save done
    public String getSaveDoneEvent(){
        return mEvents.get(mSaveDone);
    }

    public String getBufferSaveDoneEvent(){
        return mEvents.get(mBufferSaveDone);
    }
    //buffer is full event
    public String getFullEvent(){
        return mEvents.get(mFull);
    }

    /**
     * we only support one controller now
     * TODO: add suport for multiple controllers
     * @param ctr
     */
    public void addController(Controller ctr){
        this.mController = ctr;
        mEvents.put(mArrived, getCName()  +  "__" + mController.getCName() + "__arrived");
        mEvents.put(mDeparted,getCName() + "__" + mController.getCName() + "__departed");
        mEvents.put(mSaveDone,getCName()  +  "__" + mController.getCName() +"__saveDone");
        mEvents.put(mBufferSaveDone, getCName() + "__" + mController.getCName() + "__bufferSaveDone");
        mEvents.put(mFull,getCName() + "__" + mController.getCName() +  "__full");
    }

    public Model.Type getModelType() {
        return mModelType;
    }

    public List<Field> getFields() {
        return new ArrayList<>(mFields.values());
    }


    public boolean isStreaming(){
        return getModelType() == Type.STREAMING;
    }

    public boolean isLocal(){
        return getModelType() == Type.LOCAL;
    }

    public boolean isReplicated(){
        return getModelType() == Type.REPLICATED;
    }

    /***
     *
     * TODO: this all need to move out to translator
     *
     *
     */

    public List<Field> getSchema(){
        List<Field> f = new ArrayList<>();
        f.addAll(mFields.values());
        return f;
    }
    public void addField(String name, Field mFields) {
        this.mFields.put(name, mFields);
    }



    public void setProperty(Variable v) {
        LOGGER.info("Property: " + v);
        this.mPropertiesMap.put(v.getName(), v);
    }

    public int getSizeInt(){
        String size = mParameterMap.get("size");
        Integer.parseInt(size);
        return Integer.parseInt(size);
    }

    public String getSize(){
        return mParameterMap.get("size");
    }

    public int getsizeCbuffer(){
        int total=0;
        Iterator it = mFields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            total += ((Field)(pair.getValue())).getByteSize();
        }
        return total;
    }


    @Override
    public String toString(){
        String ret = "Concrete Model:" + " type : " + getTypeString() + " name: " + getVerboseName() +
                " # of Fields " + mFields.size() + "\n\t values: \n" ;
        Iterator it = mFields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ret +="\t\t" + pair.getValue().toString();
            ret+="\n";
        }
        return ret;
    }

    public String getTypeString(){
        switch (mModelType){
            case LOCAL:
                return "local";
            case STREAMING:
                return "streaming";
            case REPLICATED:
                return "replicated";
            default:
                return "Invalid";
        }
    }
    public static Model.Type getType(String name){
        switch ( name ){
            case "local":
                return Type.LOCAL;
            case "streaming":
                return Type.STREAMING;
            case "replicated":
                return Type.REPLICATED;
            default:
                return Type.tINVALID;
        }
    }
}
