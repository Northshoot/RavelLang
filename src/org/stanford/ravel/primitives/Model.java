package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.primitives.Fields.Field;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends Primitive {
    private static int idCounter = 1;

    public enum Type {
        LOCAL, STREAMING, REPLICATED, INVALID;
    }

    private static Logger LOGGER = Logger.getLogger(Model.class.getName());

    private final ModelSymbol symbol;
    private final Type mModelType;
    private final int id;

    private final Map<String, Field> mFields = new LinkedHashMap<>();
    private final Map<String, Variable> mPropertiesMap = new LinkedHashMap<>();

    public Model(String name, ModelSymbol symbol) {
        super(name,name+"Model");

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        if (mModelType == Type.INVALID)
            throw new IllegalArgumentException("Invalid model type");

        this.id = idCounter++;
    }

    public InstantiatedModel instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedModel instantiated = new InstantiatedModel(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
    }

    public int getId() {
        return id;
    }

    public org.stanford.ravel.compiler.types.Type getType() {
        return symbol.getDefinedType();
    }

    public Type getModelType() {
        return mModelType;
    }

    public List<Field> getFields() {
        return new ArrayList<>(mFields.values());
    }

    /***
     *
     * TODO: this all need to move out to translator
     *
     *
     */

    public void addField(Field field) {
        this.mFields.put(field.getName(), field);
    }

    public void setProperty(Variable v) {
        LOGGER.info("Property: " + v);
        this.mPropertiesMap.put(v.getName(), v);
    }

    @Override
    public String toString(){
        String ret = "Concrete Model:" + " type : " + mModelType + " name: " + getVerboseName() +
                " # of Fields " + mFields.size() + "\n\t values: \n" ;
        for (Map.Entry<String, Field> pair : mFields.entrySet()) {
            ret += "\t\t" + pair.getValue().toString();
            ret += "\n";
        }
        return ret;
    }
}
