package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.primitives.Fields.Field;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends Primitive {
    public enum Type {
        LOCAL, STREAMING, REPLICATED, INVALID;
    }

    private static Logger LOGGER = Logger.getLogger(Model.class.getName());
    private String mComment = "Default comments";

    private final ModelSymbol symbol;
    private final Type mModelType;

    private final Map<String, Field> mFields = new LinkedHashMap<>();
    private final Map<String, Variable> mPropertiesMap = new LinkedHashMap<>();

    /**
     * the provided api
     */
    private final String mArrived="arrived";
    private final String mDeparted="departed";
    private final String mCreate="create";
    private final String mSaveDone="save";
    private final String mBufferSaveDone="bufferSave";
    private final String mFull="full";
    private final String mDelete="delete";
    private final String mGet="get";
    private final String mFirst="first";
    private final String mLast="last";
    private final String mDestroy="destroy";
    private final Map<String, String> mEvents = new LinkedHashMap<>();

    //TODO: implement builder pattern
    public Model(String name, ModelSymbol symbol) {
        super(name,name+"Model");

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        //TODO: implement real error handling
        if (mModelType == Type.INVALID) {
            LOGGER.severe("Invalid model type!");
        }
    }

    public InstantiatedModel instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedModel instantiated = new InstantiatedModel(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
    }

    public org.stanford.ravel.compiler.types.Type getType() {
        return symbol.getDefinedType();
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

    public Type getModelType() {
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
    public void addField( Field field) {
        this.mFields.put(field.getName(), field);
    }



    public void setProperty(Variable v) {
        LOGGER.info("Property: " + v);
        this.mPropertiesMap.put(v.getName(), v);
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
    public static Type getType(String name){
        switch ( name ){
            case "local":
                return Type.LOCAL;
            case "streaming":
                return Type.STREAMING;
            case "replicated":
                return Type.REPLICATED;
            default:
                return Type.INVALID;
        }
    }
}
