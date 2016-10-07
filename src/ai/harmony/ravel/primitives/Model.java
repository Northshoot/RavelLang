package ai.harmony.ravel.primitives;

import ai.harmony.ravel.primitives.Fields.Field;

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

    //TODO: implement builder pattern
    public Model(String name, Model.Type t){
        super(name);
        //TODO: implement real error handling
        if (t == Type.tINVALID ) {
            LOGGER.severe("Invalid model type! ");
        }
        this.mModelType = t;
    }

    /**
     * we only support one controller now
     * TODO: add suport for multiple controllers
     * @param ctr
     */
    public void addController(Controller ctr){
        this.mController = ctr;
    }

    public Controller getModelController(){ return this.mController; }

    public String getModelController_name_c() { return this.getModelController().getName_c();}

    public String getModelNameUpperCase(){return getModelName().toUpperCase();}
    public Model.Type getModelType() {
        return mModelType;
    }

    public void setModelType(Model.Type mModelType) {
        this.mModelType = mModelType;
    }
    public String getService(){
        return "ravel_service";
    }
    public String getTransmitFunction(){
        return getNameLowerCase() + "_char_update";
    }
    public List<Field> getFields() {
        return new ArrayList<>(mFields.values());
    }


    public String getComment() {
        return mComment;
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
     *
     *
     *
     */
    public String getModelName(){ return mName+"Model";}

    public String getModelName_c(){ return "m_"+mName.toLowerCase()+"_model";}

    public String getModelNameCDefine(){ return mName.toUpperCase()+"_MODEL";}

    public List<Field> getSchema(){
        List<Field> f = new ArrayList<>();
        f.addAll(mFields.values());
        return f;
    }
    public void addField(String name, Field mFields) {
        this.mFields.put(name, mFields);
    }

    @Override
    public String getInitMethodCall(){
        return mName.toLowerCase()+"_model__queue" + super.getInitMethodCall();
    }

    public void setProperty(Variable v) {
        LOGGER.info("Property: " + v);
        this.mPropertiesMap.put(v.getName(), v);
    }

    public String getSize(){
        String size = mParameterMap.get("size");
        Integer.parseInt(size);
        return size;
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
