package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;

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

    private final Map<String, FieldSymbol> mFields = new HashMap<>();
    private final Map<String, Object> mConstantProperties = new HashMap<>();
    private final Map<String, String> mRefProperties = new HashMap<>();

    public Model(String name, ModelSymbol symbol) {
        super(name);

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        if (mModelType == Type.INVALID)
            throw new IllegalArgumentException("Invalid model type");

        this.id = idCounter++;
    }

    public InstantiatedModel instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedModel instantiated = new InstantiatedModel(space, this, varName);
        // TODO: check types of parameters
        instantiated.setManyParam(parameters);

        for (Map.Entry<String, Object> entry : mConstantProperties.entrySet())
            instantiated.setProperty(entry.getKey(), entry.getValue());
        // TODO: check that all parameters are set
        for (Map.Entry<String, String> entry : mRefProperties.entrySet())
            instantiated.setProperty(entry.getKey(), instantiated.getParam(entry.getValue()));
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

    // This is called by the STG templates to generate the Record class
    public Collection<FieldSymbol> getFields() {
        return Collections.unmodifiableCollection(mFields.values());
    }
    public void addField(FieldSymbol field) {
        this.mFields.put(field.getName(), field);
    }

    public void addConstantProperty(String name, Object value) {
        mConstantProperties.put(name, value);
    }
    public void addReferenceProperty(String name, String ref) {
        mRefProperties.put(name, ref);
    }

    @Override
    public String toString(){
        String ret = "Concrete Model:" + " type : " + mModelType + " name: " + getName() +
                " # of Fields " + mFields.size() + "\n\t values: \n" ;
        for (Map.Entry<String, FieldSymbol> pair : mFields.entrySet()) {
            ret += "\t\t" + pair.getValue().toString();
            ret += "\n";
        }
        return ret;
    }
}
