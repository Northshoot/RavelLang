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
    private final List<SecurityOperation> mInboundSecurityOps = new ArrayList<>();
    // a list of security operations that need to be applied to each outgoing record
    private final List<SecurityOperation> mOutboundSecurityOps = new ArrayList<>();

    private TypedIR sendCode;
    private TypedIR receiveCode;

    ConcreteModel(Space space, Model model) {
        super(space, model);
        mModel = model;

        for (ModelEvent e : ModelEvent.values())
            createEvent(e.name());
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

    void addSecurityOperation(ModelField field, Space target, Key key, SecurityPrimitive primitive, boolean isInbound) {
        assert field.getModel() == mModel;

        List<SecurityOperation> existingOps = (isInbound ? mInboundSecurityOps : mOutboundSecurityOps);

        boolean found = false;
        for (SecurityOperation op : existingOps) {
            if (op.getKey().equals(key) && op.getTarget().equals(target) && op.getPrimitive() == primitive) {
                op.addField(field);
                found = true;
                break;
            }
        }

        if (!found) {
            SecurityOperation op = new SecurityOperation(key, primitive, target);
            op.addField(field);
            existingOps.add(op);
        }
    }

    public List<SecurityOperation> getSecurityOperations(boolean isInbound) {
        return Collections.unmodifiableList((isInbound ? mInboundSecurityOps : mOutboundSecurityOps));
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
