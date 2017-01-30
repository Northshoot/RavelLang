package org.stanford.ravel.api.lang.c;

import org.stanford.ravel.api.lang.BaseIRTranslator;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.*;

/**
 * Created by gcampagn on 1/25/17.
 */
public class CCodeTranslator extends BaseIRTranslator implements LoopTreeVisitor {
    private String typeToCType(Type type) {
        if (type instanceof ArrayType) {
            return "(" + typeToCType(((ArrayType) type).getElementType()) + ")[]";
        }
        if (type instanceof ClassType.InstanceType) {
            return typeToCType(((ClassType.InstanceType) type).getClassType());
        }
        if (type instanceof PrimitiveType) {
            switch ((PrimitiveType)type) {
                case VOID:
                    return "void";
                case INT32:
                    return "int32_t";
                case BOOL:
                    return "bool";
                case DOUBLE:
                    return "double";
                case STR:
                case ERROR_MSG:
                    return "char*";
                case BYTE:
                    return "uint8_t";

                case ANY:
                case ERROR:
                default:
                    throw new AssertionError();
            }
        } else {
            return type.getName() + "*";
        }
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
        addLine(arithOp.target, " = ", arithOp.src1, arithOp.op, arithOp.src2);
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
        addLine(immediateLoad.target, " = ", immediateLoad.value.toString());
    }

    @Override
    public void visit(TMethodCall methodCall) {
        FunctionType functionType = methodCall.type;
        if (functionType.getReturnType() != PrimitiveType.VOID) {
            addCode(getRegisterName(methodCall.target));
            addCode(" = ");
        }
        addCode(functionType.getOwner().getName());
        addCode("_");
        addCode(functionType.getName());
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
