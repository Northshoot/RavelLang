package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

/**
 * A base IR translator that uses string templates
 *
 * Created by gcampagn on 1/27/17.
 */
public class STIRTranslator extends BaseIRTranslator {
    private final LiteralFormatter literalFormatter;
    private final STGroup irGroup;

    public STIRTranslator(STGroup irGroup, LiteralFormatter literalFormatter) {
        this.irGroup = irGroup;
        this.literalFormatter = literalFormatter;

        // add .name to convert registers to names
        irGroup.registerModelAdaptor(Integer.class, new ModelAdaptor() {
            @Override
            public Object getProperty(Interpreter interpreter, ST st, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
                if (propertyName.equals("name"))
                    return getRegisterName((Integer)o);
                else
                    throw new STNoSuchPropertyException(null, o, propertyName);
            }
        });
    }

    @Override
    public void declareRegister(int reg, Type type) {
        ST tmpl = this.irGroup.getInstanceOf("declaration");
        tmpl.add("reg", reg);
        tmpl.add("type", type);
        addCode(tmpl.render());
    }


    @Override
    public void visit(LoopTreeNode.Loop loop) {
        ST tmpl_begin = this.irGroup.getInstanceOf("loop_begin");
        tmpl_begin.add("loop", loop);
        addCode(tmpl_begin.render());
        loop.getBody().accept(this);
        ST tmpl_end = this.irGroup.getInstanceOf("loop_end");
        tmpl_end.add("loop", loop);
        addCode(tmpl_end.render());
    }

    @Override
    public void visit(LoopTreeNode.IfStatement ifStatement) {
        ST tmpl_begin = this.irGroup.getInstanceOf("if_stmt_begin");
        tmpl_begin.add("if_stmt", ifStatement);
        addCode(tmpl_begin.render());
        ifStatement.getIftrue().accept(this);
        ST tmpl_else = this.irGroup.getInstanceOf("if_stmt_else");
        tmpl_else.add("if_stmt", ifStatement);
        addCode(tmpl_else.render());
        ifStatement.getIffalse().accept(this);
        ST tmpl_end = this.irGroup.getInstanceOf("if_stmt_endif");
        tmpl_end.add("if_stmt", ifStatement);
        addCode(tmpl_end.render());
    }

    @Override
    public void visit(TArrayLoad arrayLoad) {
        ST tmpl = this.irGroup.getInstanceOf("array_load");
        tmpl.add("load", arrayLoad);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TArrayStore arrayStore) {
        ST tmpl = this.irGroup.getInstanceOf("array_store");
        tmpl.add("store", arrayStore);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TBinaryArithOp arithOp) {
        ST tmpl = this.irGroup.getInstanceOf("bin_op");
        tmpl.add("op", arithOp);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TBreak breakInstr) {
        ST tmpl = this.irGroup.getInstanceOf("break");
        addCode(tmpl.render());
    }

    @Override
    public void visit(TComparisonOp compOp) {
        ST tmpl = this.irGroup.getInstanceOf("bin_op");
        tmpl.add("op", compOp);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TContinue continueInstr) {
        ST tmpl = this.irGroup.getInstanceOf("continue");
        addCode(tmpl.render());
    }

    @Override
    public void visit(TConvert convert) {
        ST tmpl = this.irGroup.getInstanceOf("convert");
        tmpl.add("op", convert);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TFieldLoad fieldLoad) {
        ST tmpl = this.irGroup.getInstanceOf("field_load");
        tmpl.add("load", fieldLoad);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TFieldStore fieldStore) {
        ST tmpl = this.irGroup.getInstanceOf("field_store");
        tmpl.add("store", fieldStore);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TImmediateLoad immediateLoad) {
        ST tmpl = this.irGroup.getInstanceOf("immediate_load");
        tmpl.add("imm", immediateLoad);
        tmpl.add("literal", literalFormatter.toLiteral(immediateLoad.value));
        addCode(tmpl.render());
    }

    @Override
    public void visit(TMethodCall methodCall) {
        ST tmpl;
        if (methodCall.type.isStatic()) {
            if (methodCall.target == Registers.VOID_REG)
                tmpl = this.irGroup.getInstanceOf("void_function_call");
            else
                tmpl = this.irGroup.getInstanceOf("function_call");
        } else {
            if (methodCall.target == Registers.VOID_REG)
                tmpl = this.irGroup.getInstanceOf("void_method_call");
            else
                tmpl = this.irGroup.getInstanceOf("method_call");
        }
        tmpl.add("call", methodCall);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TMove move) {
        ST tmpl = this.irGroup.getInstanceOf("move");
        tmpl.add("op", move);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TUnaryArithOp arithOp) {
        ST tmpl = this.irGroup.getInstanceOf("unary_op");
        tmpl.add("op", arithOp);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TIntrinsic intrinsic) {
        ST tmpl;
        if (intrinsic.returnType == PrimitiveType.VOID)
            tmpl = this.irGroup.getInstanceOf("void_intrinsic");
        else
            tmpl = this.irGroup.getInstanceOf("intrinsic");
        tmpl.add("instr", intrinsic);
        addCode(tmpl.render());
    }

    @Override
    public void visit(TReturn returnInstr) {
        ST tmpl;
        if (getRegisterType(Registers.RETURN_REG) == PrimitiveType.VOID)
            tmpl = this.irGroup.getInstanceOf("void_return");
        else
            tmpl = this.irGroup.getInstanceOf("nonvoid_return");
        addCode(tmpl.render());
    }
}
