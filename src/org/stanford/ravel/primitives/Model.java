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

    private final Map<String, ModelField> mFields = new HashMap<>();
    private final Set<Flow> mFlows = new HashSet<>();
    private final Set<Space> mWriters = new HashSet<>();
    private final Set<Space> mReaders = new HashSet<>();

    public Model(String name, ModelSymbol symbol) {
        super(name);

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        if (mModelType == Type.INVALID)
            throw new IllegalArgumentException("Invalid model type");

        this.id = idCounter++;
    }

    public ModelSymbol getSymbol() {
        return symbol;
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

    public Collection<Flow> getFlows() {
        return Collections.unmodifiableCollection(mFlows);
    }

    public void addFlow(Flow f) {
        // local models only have fake flows
        assert f != null;
        assert mModelType != Type.LOCAL || f.getSource() == f.getSink();
        mFlows.add(f);

        mWriters.add(f.getSource());
        for (Space s : f) {
            if (s == f.getSource())
                continue;
            mReaders.add(s);
        }
    }

    public Collection<Flow> findFlowsForSpace(Space s) {
        List<Flow> flows = new ArrayList<>();
        for (Flow f : mFlows) {
            if (f.involvesSpace(s))
                flows.add(f);
        }
        return Collections.unmodifiableCollection(flows);
    }

    public Collection<Space> getReaders() {
        return mReaders;
    }
    public Collection<Space> getWriters() {
        return mWriters;
    }

    // This is called by the STG templates to generate the Record class
    public Collection<ModelField> getFields() {
        return Collections.unmodifiableCollection(mFields.values());
    }
    public void addField(FieldSymbol field) {
        this.mFields.put(field.getName(), new ModelField(field));
    }

    public ModelField getField(String field) {
        return mFields.get(field);
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
