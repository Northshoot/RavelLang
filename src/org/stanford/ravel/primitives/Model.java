package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends ConfigurableComponent {
    private static int idCounter = 1;

    public enum Type {
        LOCAL, STREAMING, REPLICATED, INVALID;
    }

    private static Logger LOGGER = Logger.getLogger(Model.class.getName());

    private final ModelSymbol symbol;
    private final Type mModelType;
    private final int id;
    private final Set<InstantiatedController> writers = new HashSet<>();
    private final Set<InstantiatedController> readers = new HashSet<>();

    private final Map<String, ModelField> mFields = new HashMap<>();

    public Model(String name, ModelSymbol symbol) {
        super(name);

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        if (mModelType == Type.INVALID)
            throw new IllegalArgumentException("Invalid model type");

        this.id = idCounter++;
    }

    /**
     * Retrieve the set of all writers (controllers that call .save())
     *
     * @return the set of writers
     */
    public Collection<InstantiatedController> getWriters() {
        return Collections.unmodifiableCollection(writers);
    }

    /**
     * Retrieve the set of all readers (controllers that subscribe to .arrived)
     *
     * @return the set of readers
     */
    public Collection<InstantiatedController> getReaders() {
        return Collections.unmodifiableCollection(readers);
    }

    /**
     * Mark that the given controller subscribes to arrived() events on this model
     * (making it a reader of the model, because it can see what other controllers
     * write into it, even if does nothing with it)
     *
     * @param ic the reader controller
     */
    public void addReader(InstantiatedController ic) {
        readers.add(ic);
    }

    /**
     * Mark that the given controller calls save() on this model, meaning that it
     * changes the content of records in this model, or adds records in this model
     *
     * @param ic the writer controller
     */
    public void addWriter(InstantiatedController ic) {
        writers.add(ic);
    }

    public InstantiatedModel instantiate(Space space, Map<String, Object> parameters, String varName) {
        return new InstantiatedModel(space, this, varName);
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
    public Collection<ModelField> getFields() {
        return Collections.unmodifiableCollection(mFields.values());
    }
    public void addField(FieldSymbol field) {
        this.mFields.put(field.getName(), new ModelField(field));
    }

    @Override
    public String toString() {
        String ret = "Concrete Model:" + " type : " + mModelType + " name: " + getName() +
                " # of Fields " + mFields.size() + "\n\t values: \n" ;
        for (Map.Entry<String, ModelField> pair : mFields.entrySet()) {
            ret += "\t\t" + pair.getValue().toString();
            ret += "\n";
        }
        return ret;
    }
}
