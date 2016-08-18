package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends Primitive {
    ModelType mModelType;
    String mName;
    private List<Field> mFields;
    private List<Property> mProperties;


    public ModelType getModelType() {
        return mModelType;
    }

    public void setmModelType(ModelType mModelType) {
        this.mModelType = mModelType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public List<Field> getmFields() {
        return mFields;
    }

    public void setFields(List<Field> mFields) {
        this.mFields = mFields;
    }



    public void setField(Field mFields) {
        this.mFields.add(mFields);
    }


    public void setProperty(Property mProperties) {
        this.mProperties.add(mProperties);
    }

    public Model(){

    }



}
