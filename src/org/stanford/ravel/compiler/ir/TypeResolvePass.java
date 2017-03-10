package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ParserUtils;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.ir.untyped.*;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;

/**
 * Converts tree-like UntypedIR to TypedIR, checking the type of
 * expressions and converting to control flow graph in the process
 *
 * Created by gcampagn on 1/23/17.
 */
public class TypeResolvePass implements InstructionVisitor {
    private final CompileToIRPass compiler;

    private final TypedIRBuilder ir = new TypedIRBuilder();
    private final ControlFlowGraphBuilder cfgBuilder = ir.getControlFlowGraphBuilder();
    private final LoopTreeBuilder loopTreeBuilder = ir.getLoopTreeBuilder();
    private TBlock currentLoopHead = null;
    private TBlock currentLoopContinuation = null;

    public TypeResolvePass(CompileToIRPass compiler) {
        this.compiler = compiler;
    }

    public void declareParameter(VariableSymbol sym, boolean isClassScope) {
        assert sym.getRegister() != Registers.UNSET_REG;
        assert sym.getType() != PrimitiveType.ANY;
        ir.declareParameter(sym, isClassScope);
    }

    public void declareTemporary(VariableSymbol sym) {
        Type type = sym.getType();
        if (type == PrimitiveType.ANY)
            return;
        if (sym.getRegister() == Registers.UNSET_REG)
            return;
        if (type instanceof ClassType)
            type = ((ClassType) type).getInstanceType();

        // go straight to ir.setRegisterType so we don't do the thing with temporaries
        ir.setRegisterType(sym.getRegister(), type);
    }

    public TypedIR run(UntypedIR ir) {
        this.ir.setNextRegister(ir.numUsedRegisters());
        ir.getRoot().accept(this);
        return this.ir.finish();
    }

    void setReturnType(Type type) {
        setRegisterType(Registers.RETURN_REG, type);
    }
    private void setRegisterType(int reg, Type type) {
        if (type instanceof ArrayType) {
            if (((ArrayType) type).isKnownBound()) {
                // remove bounds from temporaries, so that we declare them as pointers, not stack allocated
                type = new ArrayType(((ArrayType) type).getElementType());
            }
        }

        ir.setRegisterType(reg, type);
    }
    private Type getRegisterType(int reg) {
        return ir.getRegisterType(reg);
    }
    private int allocateRegister(Type type) {
        return ir.allocateRegister(type);
    }

    private void typeError(Instruction instr, String message) {
        compiler.emitError(new SourceLocation(instr.definer), message);
    }

    @Override
    public void visit(ArrayLoad instr) {
        Type ownerType = getRegisterType(instr.source);
        if (!(ownerType instanceof ArrayType)) {
            typeError(instr, ownerType.getName() + " is not an array");
            return;
        }

        Type indexType = getRegisterType(instr.index);
        int index;
        if (indexType == PrimitiveType.INT32) {
            index = instr.index;
        } else if (PrimitiveType.INT32.isAssignable(indexType)) {
            index = allocateRegister(PrimitiveType.INT32);
            ir.add(new TConvert(PrimitiveType.INT32, indexType, index, instr.index));
        } else {
            typeError(instr, "array index must be an integer, not " + indexType.getName());
            index = Registers.ERROR_REG;
        }

        Type resultType = ((ArrayType) ownerType).getElementType();

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of array index to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TArrayLoad(resultType, (ArrayType)ownerType, target, instr.source, index));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(ArrayStore instr) {
        Type ownerType = getRegisterType(instr.object);
        if (!(ownerType instanceof ArrayType)) {
            typeError(instr, ownerType.getName() + " is not an array");
            return;
        }

        Type indexType = getRegisterType(instr.index);
        int index;
        if (indexType == PrimitiveType.INT32) {
            index = instr.index;
        } else if (PrimitiveType.INT32.isAssignable(indexType)) {
            index = allocateRegister(PrimitiveType.INT32);
            ir.add(new TConvert(PrimitiveType.INT32, indexType, index, instr.index));
        } else {
            typeError(instr, "array index must be an integer, not " + indexType.getName());
            index = Registers.ERROR_REG;
        }

        ArrayType arrayType = (ArrayType) ownerType;

        if (!arrayType.isMutable()) {
            typeError(instr, "array type " + arrayType.getName() + " is not writable");
            return;
        }

        Type targetType = arrayType.getElementType();
        Type sourceType = getRegisterType(instr.value);
        int value;
        if (targetType.equals(sourceType)) {
            value = instr.value;
        } else if (targetType.isAssignable(sourceType)) {
            value = allocateRegister(targetType);
            ir.add(new TConvert(targetType, sourceType, value, instr.value));
        } else {
            typeError(instr, "cannot assign expression of type " + sourceType.getName() + " to an element of array of type " + arrayType.getName());
            value = Registers.ERROR_REG;
        }
        ir.add(new TArrayStore(targetType, arrayType, instr.object, index, value));
    }

    private Type promote(Type t1, Type t2) {
        // convert bool -> byte -> int -> double
        if (t1.equals(t2))
            return t1;
        if (t1.isAssignable(t2))
            return t1;
        if (t2.isAssignable(t1))
            return t2;

        return PrimitiveType.ERROR;
    }

    private static Type adjustTypeForBinaryOp(BinaryOperation op, Type resultType) {
        if (resultType == PrimitiveType.ERROR)
            return resultType;
        if (!(resultType instanceof PrimitiveType))
            return PrimitiveType.ERROR;

        if (op.isBitwise()) {
            if (resultType == PrimitiveType.INT32)
                return resultType;
            if (resultType == PrimitiveType.BYTE)
                return resultType;
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            return PrimitiveType.ERROR;
        }
        if (op.isNumeric()) {
            if (resultType == PrimitiveType.BOOL || resultType == PrimitiveType.BYTE) {
                if (op == BinaryOperation.POW || op == BinaryOperation.DIV)
                    return PrimitiveType.DOUBLE;
                else
                    return PrimitiveType.INT32;
            }
            if (resultType != PrimitiveType.INT32 && resultType != PrimitiveType.DOUBLE)
                return PrimitiveType.ERROR;
            if (op == BinaryOperation.POW || op == BinaryOperation.DIV)
                return PrimitiveType.DOUBLE;
            return resultType;
        }
        if (op == BinaryOperation.ADD && (resultType == PrimitiveType.BOOL || resultType == PrimitiveType.BYTE))
            return PrimitiveType.INT32;

        return resultType;
    }

    @Override
    public void visit(BinaryArithOp instr) {
        Type srcType1 = getRegisterType(instr.src1);
        Type srcType2 = getRegisterType(instr.src2);

        Type resultType = promote(srcType1, srcType2);
        resultType = adjustTypeForBinaryOp(instr.op, resultType);
        if (resultType == PrimitiveType.ERROR) {
            typeError(instr, "invalid operand types " + srcType1.getName() + " and " + srcType2.getName() + " to binary " + instr.op);
        }

        int src1;
        if (!resultType.equals(srcType1)) {
            src1 = allocateRegister(resultType);
            ir.add(new TConvert(resultType, srcType1, src1, instr.src1));
        } else {
            src1 = instr.src1;
        }
        int src2;
        if (!resultType.equals(srcType2)) {
            src2 = allocateRegister(resultType);
            ir.add(new TConvert(resultType, srcType2, src2, instr.src2));
        } else {
            src2 = instr.src2;
        }

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of operation " + instr.op + " (promoted type " + resultType.getName() + ") to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TBinaryArithOp(resultType, target, src1, src2, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    private static Type adjustTypeForComparisonOp(ComparisonOperation op, Type resultType) {
        if (resultType == PrimitiveType.ERROR)
            return resultType;
        if (op == ComparisonOperation.EQUAL || op == ComparisonOperation.NOTEQUAL)
            return resultType;
        if (!(resultType instanceof PrimitiveType))
            return PrimitiveType.ERROR;

        if (resultType == PrimitiveType.BOOL)
            return PrimitiveType.INT32;
        if (resultType == PrimitiveType.BYTE || resultType == PrimitiveType.INT32 || resultType == PrimitiveType.DOUBLE
                || resultType == PrimitiveType.STR)
            return resultType;
        return PrimitiveType.ERROR;
    }

    @Override
    public void visit(ComparisonOp instr) {
        Type srcType1 = getRegisterType(instr.src1);
        Type srcType2 = getRegisterType(instr.src2);

        Type opType = promote(srcType1, srcType2);
        opType = adjustTypeForComparisonOp(instr.op, opType);
        Type resultType = PrimitiveType.BOOL;
        if (opType == PrimitiveType.ERROR) {
            typeError(instr, "invalid operand types " + srcType1.getName() + " and " + srcType2.getName() + " for comparison " + instr.op);
        }

        int src1;
        if (!opType.equals(srcType1)) {
            src1 = allocateRegister(opType);
            ir.add(new TConvert(opType, srcType1, src1, instr.src1));
        } else {
            src1 = instr.src1;
        }
        int src2;
        if (!opType.equals(srcType2)) {
            src2 = allocateRegister(opType);
            ir.add(new TConvert(opType, srcType2, src2, instr.src2));
        } else {
            src2 = instr.src2;
        }

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of comparison " + instr.op + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TComparisonOp(opType, target, src1, src2, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    private void handleArrayLength(FieldLoad instr) {
        Type ownerType = getRegisterType(instr.source);
        Type resultType = PrimitiveType.INT32;

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of field load " + ownerType.getName() + "." + instr.field + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(IntrinsicFactory.createArrayLength((ArrayType)ownerType, target, instr.source));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    private void handleStringLength(FieldLoad instr) {
        Type ownerType = getRegisterType(instr.source);
        Type resultType = PrimitiveType.INT32;

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of field load " + ownerType.getName() + "." + instr.field + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(IntrinsicFactory.createStringLength(target, instr.source));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(FieldLoad instr) {
        Type ownerType = getRegisterType(instr.source);
        if (!(ownerType instanceof CompoundType)) {
            if (instr.field.equals("length")) {
                if (ownerType instanceof ArrayType) {
                    handleArrayLength(instr);
                    return;
                } else if (ownerType == PrimitiveType.STR) {
                    handleStringLength(instr);
                    return;
                }
            }

            typeError(instr, "cannot read field " + instr.field + " on non compound type " + ownerType.getName());
            return;
        }

        CompoundType compoundType = (CompoundType) ownerType;
        Type resultType = compoundType.getMemberType(instr.field);
        if (resultType == null) {
            typeError(instr, ownerType.getName() + " has no field " + instr.field);
            resultType = PrimitiveType.ERROR;
        }
        if (resultType instanceof FunctionType) {
            typeError(instr, "cannot read method " + ownerType.getName() + "." + instr.field + " as a field");
            resultType = PrimitiveType.ERROR;
        }

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of field load " + ownerType.getName() + "." + instr.field + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TFieldLoad(resultType, compoundType, target, instr.source, instr.field));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(FieldStore instr) {
        Type ownerType = getRegisterType(instr.object);
        if (!(ownerType instanceof CompoundType)) {
            typeError(instr, "cannot write field " + instr.field + " on non compound type " + ownerType.getName());
            return;
        }

        CompoundType compoundType = (CompoundType) ownerType;
        Type targetType = compoundType.getMemberType(instr.field);
        if (targetType == null) {
            typeError(instr, ownerType.getName() + " has no field " + instr.field);
            return;
        }
        if (targetType instanceof FunctionType) {
            typeError(instr, "cannot write method " + ownerType.getName() + "." + instr.field + " as a field");
            return;
        }

        if (!compoundType.isWritable(instr.field)) {
            typeError(instr, "field " + ownerType.getName() + "." + instr.field + " is not writable");
            return;
        }

        Type sourceType = getRegisterType(instr.value);
        int value;
        if (targetType.equals(sourceType)) {
            value = instr.value;
        } else if (targetType.isAssignable(sourceType)) {
            value = allocateRegister(targetType);
            ir.add(new TConvert(targetType, sourceType, value, instr.value));
        } else {
            typeError(instr, "cannot assign expression of type " + sourceType.getName() + " to a field " + ownerType.getName() + "." + instr.field + " of type " + targetType.getName());
            value = Registers.ERROR_REG;
        }
        ir.add(new TFieldStore(targetType, compoundType, instr.object, instr.field, value));
    }

    @Override
    public void visit(MethodCall instr) {
        Type ownerType = getRegisterType(instr.owner);
        if (!(ownerType instanceof CompoundType)) {
            typeError(instr, "cannot call method " + instr.method + " on non compound type " + ownerType.getName());
            return;
        }

        CompoundType compoundType = (CompoundType) ownerType;
        Type methodType = compoundType.getMemberType(instr.method);
        if (!(methodType instanceof FunctionType)) {
            typeError(instr, ownerType.getName() + "." + instr.method + " is not a method");
            return;
        }

        FunctionType functionType = (FunctionType)methodType;
        Type[] argumentTypes = functionType.getArgumentTypes();
        if (instr.arguments.length != argumentTypes.length) {
            typeError(instr, ownerType.getName() + "." + instr.method + " takes " + argumentTypes.length + " arguments, " + instr.arguments.length + " passed");
        }
        int[] arguments = new int[argumentTypes.length];
        for (int i = 0; i < argumentTypes.length; i++) {
            int arg = i < instr.arguments.length ? instr.arguments[i] : Registers.ERROR_REG;
            Type formalType = argumentTypes[i];

            arguments[i] = arg;

            if (arg != Registers.ERROR_REG) {
                Type argType = getRegisterType(arg);
                if (!formalType.isAssignable(argType)) {
                    typeError(instr, "cannot convert " + argType.getName() + " to " + formalType.getName() + " in argument " + (i+1) + " of " + ownerType.getName() + "." + instr.method);
                    arguments[i] = Registers.ERROR_REG;
                } else if (!formalType.equals(argType)) {
                    arguments[i] = allocateRegister(formalType);
                    ir.add(new TConvert(formalType, argType, arguments[i], arg));
                }
            }
        }

        Type resultType = functionType.getReturnType();

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            setRegisterType(instr.target, resultType);
            if (resultType != PrimitiveType.VOID)
                target = instr.target;
            else
                target = Registers.VOID_REG;
            targetType = resultType;
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of method call " + ownerType.getName() + "." + instr.method + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        if (resultType == PrimitiveType.VOID) {
            assert targetType == resultType || target == Registers.ERROR_REG;
            target = Registers.VOID_REG;
        }

        ir.add(new TMethodCall(functionType, target, instr.owner, instr.method, arguments));
        if (target != instr.target && target != Registers.ERROR_REG && target != Registers.VOID_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(IfStatement instr) {
        Type condType = getRegisterType(instr.cond);

        int cond;
        if (condType == PrimitiveType.BOOL) {
            cond = instr.cond;
        } else if (PrimitiveType.BOOL.isAssignable(condType)) {
            cond = allocateRegister(PrimitiveType.BOOL);
            ir.add(new TConvert(PrimitiveType.BOOL, condType, cond, instr.cond));
        } else {
            typeError(instr, "condition in if statement must be a boolean (found " + condType.getName() + ")");
            cond = Registers.ERROR_REG;
        }

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(cond, iftrue, iffalse);
        ir.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);
        instr.iftrue.accept(this);
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();


        loopTreeBuilder.elseStatement(cond);
        cfgBuilder.pushBlock(iffalse);
        instr.iffalse.accept(this);
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(cond);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    @Override
    public void visit(ImmediateLoad instr) {
        Type literalType = ParserUtils.typeFromLiteral(instr.value);

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, literalType);
        } else if (targetType.equals(literalType)) {
            target = instr.target;
        } else if (targetType.isAssignable(literalType)) {
            target = allocateRegister(literalType);
        } else {
            typeError(instr, "cannot assign literal of type " + literalType.getName() + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TImmediateLoad(literalType, target, instr.value));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, literalType, instr.target, target));
    }

    @Override
    public void visit(Move instr) {
        Type sourceType = getRegisterType(instr.source);

        boolean convert;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            convert = false;
            targetType = sourceType;
            setRegisterType(instr.target, sourceType);
        } else if (targetType.equals(sourceType)) {
            convert = false;
        } else if (targetType.isAssignable(sourceType)) {
            convert = true;
        } else {
            typeError(instr, "cannot assign value of type " + sourceType.getName() + " to a variable of type " + targetType.getName());
            convert = true;
        }

        if (ir.isParameter(instr.target)) {
            if (convert) {
                int tmp = allocateRegister(targetType);
                ir.add(new TConvert(targetType, sourceType, tmp, instr.source));
                ir.add(IntrinsicFactory.createParameterSet(targetType, instr.target, tmp));
            } else {
                ir.add(IntrinsicFactory.createParameterSet(targetType, instr.target, instr.source));
            }
        } else {
            if (convert)
                ir.add(new TConvert(targetType, sourceType, instr.target, instr.source));
            else
                ir.add(new TMove(targetType, instr.target, instr.source));
        }
    }

    @Override
    public void visit(ExplicitCast instr) {
        Type sourceType = getRegisterType(instr.source);
        Type castType = instr.targetType;

        boolean convert;
        if (castType == PrimitiveType.ANY) {
            convert = false;
            castType = sourceType;
            setRegisterType(instr.target, sourceType);
        } else if (castType.equals(sourceType)) {
            convert = false;
        } else if (castType instanceof PrimitiveType && sourceType instanceof PrimitiveType) {
            convert = true;
        } else {
            typeError(instr, "cannot cast from " + sourceType.getName() + " to " + castType.getName());
            convert = true;
        }

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, castType);
        } else if (targetType.equals(castType)) {
            target = instr.target;
        } else if (targetType.isAssignable(castType)) {
            target = allocateRegister(castType);
        } else {
            typeError(instr, "cannot assign value of type " + castType.getName() + " to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        if (convert)
            ir.add(new TConvert(castType, sourceType, target, instr.source));
        else
            ir.add(new TMove(castType, target, instr.source));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, castType, instr.target, target));
    }

    private static Type adjustTypeForUnaryOp(UnaryOperation op, Type resultType) {
        if (resultType == PrimitiveType.ERROR)
            return resultType;
        if (!(resultType instanceof PrimitiveType))
            return PrimitiveType.ERROR;

        if (op.isBitwise()) {
            if (resultType == PrimitiveType.INT32)
                return resultType;
            if (resultType == PrimitiveType.BYTE)
                return resultType;
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            return PrimitiveType.ERROR;
        }
        if (op.isNumeric()) {
            if (resultType == PrimitiveType.BOOL || resultType == PrimitiveType.BYTE)
                return PrimitiveType.INT32;
            if (resultType != PrimitiveType.INT32 && resultType != PrimitiveType.DOUBLE)
                return PrimitiveType.ERROR;
            return resultType;
        }
        if (op == UnaryOperation.NOT)
            return PrimitiveType.BOOL;

        return resultType;
    }

    @Override
    public void visit(UnaryArithOp instr) {
        Type srcType = getRegisterType(instr.source);

        Type resultType = adjustTypeForUnaryOp(instr.op, srcType);
        if (resultType == PrimitiveType.ERROR) {
            typeError(instr, "invalid operand type " + srcType.getName() + " to unary " + instr.op);
        }

        int src;
        if (!resultType.equals(srcType)) {
            src = allocateRegister(resultType);
            ir.add(new TConvert(resultType, srcType, src, instr.source));
        } else {
            src = instr.source;
        }

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
        } else if (targetType.equals(resultType)) {
            target = instr.target;
        } else if (targetType.isAssignable(resultType)) {
            target = allocateRegister(resultType);
        } else {
            typeError(instr, "cannot assign result of operation " + instr.op + " (promoted type " + resultType.getName() + ") to a variable of type " + targetType.getName());
            target = Registers.ERROR_REG;
        }

        ir.add(new TUnaryArithOp(resultType, target, src, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            ir.add(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(WhileLoop instr) {
        TBlock saveLoopHead = currentLoopHead;
        TBlock saveLoopContinuation = currentLoopContinuation;

        TBlock loopHead = cfgBuilder.newBlock();
        currentLoopHead = loopHead;

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();
        currentLoopContinuation = continuation;

        loopTreeBuilder.beginLoop(loopHead);
        cfgBuilder.addSuccessor(loopHead);

        cfgBuilder.pushBlock(loopHead);
        instr.head.accept(this);

        Type condType = getRegisterType(instr.cond);

        int cond;
        if (condType == PrimitiveType.BOOL) {
            cond = instr.cond;
        } else if (PrimitiveType.BOOL.isAssignable(condType)) {
            cond = allocateRegister(PrimitiveType.BOOL);
            ir.add(new TConvert(PrimitiveType.BOOL, condType, cond, instr.cond));
        } else {
            typeError(instr, "condition in if statement must be a boolean (found " + condType.getName() + ")");
            cond = Registers.ERROR_REG;
        }

        // to facilitate the LoopTree construction, we lower every loop
        // to the form
        //
        // while(true) {
        //    <loopHead>
        //    if (cond) {}
        //    else { break }
        //    <loopBody>
        // }

        TBlock empty = cfgBuilder.newBlock();
        TBlock breakBlock = cfgBuilder.newBlock();
        TBlock loopBody = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(empty);
        cfgBuilder.addSuccessor(breakBlock);
        TIfStatement ifStatement = new TIfStatement(cond, empty, breakBlock);
        ir.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, empty, breakBlock);
        cfgBuilder.pushBlock(empty);
        cfgBuilder.addSuccessor(loopBody);
        cfgBuilder.popBlock();
        loopTreeBuilder.elseStatement(cond);
        cfgBuilder.pushBlock(breakBlock);
        ir.add(new TBreak());
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();
        loopTreeBuilder.endIfStatement(cond);

        cfgBuilder.replaceBlock(loopBody);
        loopTreeBuilder.addBasicBlock(loopBody);
        instr.body.accept(this);
        cfgBuilder.addSuccessor(loopHead);
        cfgBuilder.popBlock();

        loopTreeBuilder.endLoop();
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);

        currentLoopHead = saveLoopHead;
        currentLoopContinuation = saveLoopContinuation;
    }

    @Override
    public void visit(Break instr) {
        if (currentLoopContinuation == null) {
            typeError(instr, "break statement not inside a loop");
            return;
        }

        ir.add(new TBreak());

        // code after break will be dead, but we need a block to appease CfgBuilder
        TBlock afterBreak = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(currentLoopContinuation);
        loopTreeBuilder.addDeadBlock(afterBreak);
        cfgBuilder.replaceBlock(afterBreak);
    }

    @Override
    public void visit(Continue instr) {
        if (currentLoopHead == null) {
            typeError(instr, "continue statement not inside a loop");
            return;
        }

        ir.add(new TContinue());

        // see comment in visit(Break)
        TBlock afterContinue = cfgBuilder.newBlock();
        cfgBuilder.addSuccessor(currentLoopHead);
        loopTreeBuilder.addDeadBlock(afterContinue);
        cfgBuilder.replaceBlock(afterContinue);
    }
}
