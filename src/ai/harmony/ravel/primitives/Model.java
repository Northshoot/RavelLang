package ai.harmony.ravel.primitives;

import ai.harmony.ravel.primitives.Fields.Field;

import java.util.*;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends Primitive {

    public enum Type { LOCAL, STREAMING, REPLICATED, tINVALID }

    Model.Type mModelType;
    private Map<String, Field> mFields = new LinkedHashMap<>();
    private Map<String, Property> mProperties = new LinkedHashMap<>();


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

    public void addField(String name, Field mFields) {
        this.mFields.put(name, mFields);
    }


    public void setProperty(String name, Property mProperties) {
        //TODO: implement property setter
        this.mProperties.put(name, mProperties);
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
    public Model(String name, Model.Type t){
        super(name);
        //TODO: implement real error handling
        if (t == Type.tINVALID ) {
            System.err.println("Invalid model type! ");
        }
        this.mModelType = t;
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
