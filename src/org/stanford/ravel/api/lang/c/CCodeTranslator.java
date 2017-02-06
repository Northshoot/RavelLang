package org.stanford.ravel.api.lang.c;

import org.stanford.ravel.api.lang.BaseIRTranslator;
import org.stanford.ravel.api.lang.LiteralFormatter;
import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * Convert IR to C code.
 *
 * FIXME: should be replaced with string templates
 *
 * Created by gcampagn on 1/25/17.
 */
public class CCodeTranslator extends BaseIRTranslator {
    private final AttributeRenderer typeRenderer;
    private final AttributeRenderer identRenderer;
    private final LiteralFormatter literalFormatter;

    public CCodeTranslator(AttributeRenderer typeRenderer, AttributeRenderer identRenderer, LiteralFormatter literalFormatter) {
        this.typeRenderer = typeRenderer;
        this.identRenderer = identRenderer;
        this.literalFormatter = literalFormatter;
    }

    private String typeToCType(Type type) {
        return typeRenderer.toString(type, null, Locale.getDefault());
    }

    private String nameToUnderscore(String name) {
        return identRenderer.toString(name, "function", Locale.getDefault());
    }

    // override declare controller scope to add "this->", so that references in the code
    // are correct always
    @Override
    protected void declareControllerScope(VariableSymbol sym) {
        setRegisterName(sym.getRegister(), "this->" + sym.getName());
    }

    @Override
    public void declareRegister(int reg, Type type) {
        addCode(typeToCType(type));
        addCode(" ");
        addCode(getRegisterName(reg));
        addCode(";\n");
    }

    @Override
    public void visit(LoopTreeNode.Loop loop) {
        addCode("while (true) {\n");
        loop.getBody().accept(this);
        addCode("}\n");
    }

    @Override
    public void visit(LoopTreeNode.IfStatement ifStatement) {
        addCode("if (");
        addCode(getRegisterName(ifStatement.getCondition()));
        addCode(") {\n");
        ifStatement.getIftrue().accept(this);
        addCode("} else {\n");
        ifStatement.getIffalse().accept(this);
        addCode("}\n");
    }

    private void addLine(Object... elements) {
        for (Object el : elements) {
            if (el instanceof Integer)
                addCode(getRegisterName(((Integer) el)));
            else
                addCode(el.toString());
        }
        addCode(";\n");
    }

    @Override
    public void visit(TArrayLoad arrayLoad) {
        addLine(arrayLoad.target, " = ", arrayLoad.source, "[", arrayLoad.index, "]");
    }

    @Override
    public void visit(TArrayStore arrayStore) {
        addLine(arrayStore.object, "[", arrayStore.index, "] =", arrayStore.value);
    }

    @Override
    public void visit(TBinaryArithOp arithOp) {
        if (arithOp.op == BinaryOperation.ADD && arithOp.type == PrimitiveType.STR) {
            addLine(arithOp.target, " = malloc(strlen(", arithOp.src1, ") + strlen(", arithOp.src2, ") + 1)");
            addLine("if (", arithOp.target, " == NULL) abort() /* FIXME */");
            addLine("stpcpy(stpcpy(", arithOp.target, ", ", arithOp.src1, "), ", arithOp.src2, ")");
        } else {
            addLine(arithOp.target, " = ", arithOp.src1, arithOp.op, arithOp.src2);
        }
    }

    @Override
    public void visit(TBreak breakInstr) {
        addLine("break");
    }

    @Override
    public void visit(TComparisonOp compOp) {
        addLine(compOp.target, " = ", compOp.src1, compOp.op, compOp.src2);
    }

    @Override
    public void visit(TContinue continueInstr) {
        addLine("continue");
    }

    @Override
    public void visit(TConvert convert) {
        addLine(convert.target, " = (", typeToCType(convert.tgtType), ") ", convert.source);
    }

    @Override
    public void visit(TFieldLoad fieldLoad) {
        addLine(fieldLoad.target, " = ", fieldLoad.source, "->", fieldLoad.field);
    }

    @Override
    public void visit(TFieldStore fieldStore) {
        addLine(fieldStore.object, "->", fieldStore.field, " = ", fieldStore.value);
    }

    @Override
    public void visit(TImmediateLoad immediateLoad) {
        addLine(immediateLoad.target, " = ", literalFormatter.toLiteral(immediateLoad.value));
    }

    @Override
    public void visit(TMethodCall methodCall) {
        FunctionType functionType = methodCall.type;
        if (functionType.getReturnType() != PrimitiveType.VOID) {
            addCode(getRegisterName(methodCall.target));
            addCode(" = ");
        }
        String ownerName = nameToUnderscore(functionType.getOwner().getName());
        if (functionType.getOwner() instanceof ModelType ||
                functionType.getOwner() instanceof InterfaceType)
            ownerName = "ravel_generated_" + ownerName;
        else
            ownerName = "ravel_" + ownerName;
        addCode(ownerName);
        addCode("_");
        addCode(functionType.getFunctionName());
        addCode("(");
        boolean first = true;
        if (!functionType.isStatic()) {
            addCode(getRegisterName(methodCall.owner));
            first = false;
        }
        for (int arg : methodCall.arguments) {
            if (!first)
                addCode(", ");
            addCode(getRegisterName(arg));
        }
        addCode(");\n");
    }

    @Override
    public void visit(TMove move) {
        addLine(move.target, " = ", move.source);
    }

    @Override
    public void visit(TUnaryArithOp arithOp) {
        addLine(arithOp.target, " = ", arithOp.op, arithOp.source);
    }
}
