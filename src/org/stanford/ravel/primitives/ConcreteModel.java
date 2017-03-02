package org.stanford.ravel.primitives;

import org.stanford.ravel.analysis.security.Key;
import org.stanford.ravel.analysis.security.SecurityOperation;
import org.stanford.ravel.analysis.security.SecurityPrimitive;
import org.stanford.ravel.compiler.ir.typed.TypedIR;

import java.util.*;

/**
 * Created by gcampagn on 1/26/17.
 */
public class ConcreteModel extends BaseEventComponent {
    private final Model mModel;
    private final Set<Space> mStreamingSinks = new HashSet<>();
    private final Set<Space> mStreamingSources = new HashSet<>();

    // a list of security operations that need to be applied to each incoming record
    private final Map<SecurityPrimitive, List<SecurityOperation>> mSecurityOps = new HashMap<>();
    // a list of security operations that need to be applied to each outgoing record
    private final List<SecurityOperation> mOutboundSecurityOps = new ArrayList<>();

    private TypedIR encryptCode;
    private TypedIR decryptCode;

    ConcreteModel(Space space, Model model) {
        super(space, model);
        mModel = model;

        for (ModelEvent e : ModelEvent.values())
            createEvent(e.name());

        for (SecurityPrimitive prim : SecurityPrimitive.values())
            mSecurityOps.put(prim, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Concrete Model " + getSpace().getName() + "." + getName();
    }

    public Model getBaseModel() {
        return mModel;
    }

    public void addStreamingSink(Space target) {
        assert mModel.getModelType() != Model.Type.LOCAL;
        assert target != getSpace();
        mStreamingSinks.add(target);
    }
    public Collection<Space> getStreamingSinks() {
        return Collections.unmodifiableCollection(mStreamingSinks);
    }

    public void addStreamingSource(Space target) {
        assert mModel.getModelType() != Model.Type.LOCAL;
        assert target != getSpace();
        mStreamingSources.add(target);
    }
    public Collection<Space> getStreamingSources() {
        return Collections.unmodifiableCollection(mStreamingSources);
    }

    void addSecurityOperation(ModelField field, Space target, Flow flow, Key key, SecurityPrimitive primitive, int offset) {
        assert field.getModel() == mModel;

        List<SecurityOperation> existingOps = mSecurityOps.get(primitive);

        boolean found = false;
        for (SecurityOperation op : existingOps) {
            if (op.getKey().equals(key) && op.getTarget().equals(target) && op.getPrimitive() == primitive &&
                    op.getOffset() == offset) {
                // if target is equal, then flow must also be equal
                //
                // (see the note at the end of SecurityAnalysis.computeMacForField for details)
                assert flow == op.getFlow();
                op.addField(field);
                found = true;
                break;
            }
        }

        if (!found) {
            SecurityOperation op = new SecurityOperation(key, primitive, target, flow, offset);
            op.addField(field);
            existingOps.add(op);
        }
    }

    public List<SecurityOperation> getSecurityOperations(SecurityPrimitive primitive) {
        return Collections.unmodifiableList(mSecurityOps.get(primitive));
    }

    public TypedIR getEncryptCode() {
        return encryptCode;
    }
    public void setEncryptCode(TypedIR code) {
        encryptCode = code;
    }

    public TypedIR getDecryptCode() {
        return decryptCode;
    }
    public void setDecryptCode(TypedIR code) {
        decryptCode = code;
    }
}
