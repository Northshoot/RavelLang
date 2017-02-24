package org.stanford.ravel.primitives;

import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.types.ModelType;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by lauril on 7/21/16.
 */
public class Model extends ConfigurableComponent {
    public final static int DEFAULT_MODEL_SIZE = 10;

    private static int idCounter = 1;

    public enum Type {
        LOCAL, STREAMING, REPLICATED, INVALID;
    }

    private static Logger LOGGER = Logger.getLogger(Model.class.getName());

    private final ModelSymbol symbol;
    private final Type mModelType;
    private final int id;

    private final Map<String, ModelField> mFields = new HashMap<>();
    private final List<ModelField> mFieldList = new ArrayList<>();
    private final Set<Flow> mFlows = new HashSet<>();
    private final Set<Space> mWriters = new HashSet<>();
    private final Set<Space> mReaders = new HashSet<>();

    private TypedIR sendCode;
    private TypedIR receiveCode;

    public Model(String name, ModelSymbol symbol) {
        super(name);

        this.symbol = symbol;
        this.mModelType = symbol.getModelType();

        if (mModelType == Type.INVALID)
            throw new IllegalArgumentException("Invalid model type");

        this.id = idCounter++;

        // set defaults
        setConstantProperty("records", DEFAULT_MODEL_SIZE);
        setConstantProperty("reliable", false);
        setConstantProperty("durable", false);
    }

    public Object getSize() {
        return getProperty("records");
    }
    public Object getReliable() {
        return getProperty("reliable");
    }
    public Object getDurable() {
        return getProperty("durable");
    }

    public ModelSymbol getSymbol() {
        return symbol;
    }

    public ConcreteModel instantiate(Space space) {
        return new ConcreteModel(space, this);
    }

    public int getId() {
        return id;
    }

    public ModelType getType() {
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
        return Collections.unmodifiableCollection(mFieldList);
    }
    public void addField(FieldSymbol fieldSym) {
        ModelField newField = new ModelField(fieldSym, this);
        ModelField previous = this.mFields.put(newField.getName(), newField);
        assert previous == null;
        this.mFieldList.add(newField);
    }

    public ModelField getField(String field) {
        return mFields.get(field);
    }

    @Override
    public String toString() {
        String ret = "Concrete Model:" + " type : " + mModelType + " name: " + getName() +
                " # of Fields " + mFieldList.size() + "\n\t values: \n" ;
        for (ModelField field : mFieldList) {
            ret += "\t\t" + field.toString();
            ret += "\n";
        }
        return ret;
    }

    public void packFields() {
        // sort fields so that the smallest goes last
        //
        // variable length fields have size -1 so they always sort last
        this.mFieldList.sort((f1, f2) -> {
            int sz1 = f1.getType().getSerializedSize();
            int sz2 = f2.getType().getSerializedSize();

            return sz2 - sz1;
        });

        int off = 0;
        List<ModelField> addends = new ArrayList<>();
        boolean isVariable = false;
        for (ModelField field : mFieldList) {
            field.setOffset(new ModelField.FieldOffset(off, addends));

            int size = field.getType().getSerializedSize();
            assert !isVariable || size < 0;
            if (size < 0) {
                isVariable = true;
                addends.add(field);
            } else {
                off += size;
            }
        }
    }

    public void setSendCode(TypedIR sendCode) {
        this.sendCode = sendCode;
    }
    public TypedIR getSendCode() {
        return sendCode;
    }
    public void setReceiveCode(TypedIR receiveCode) {
        this.receiveCode = receiveCode;
    }
    public TypedIR getReceiveCode() {
        return receiveCode;
    }
}
