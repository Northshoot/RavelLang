package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ControllerEventCompiler;
import org.stanford.ravel.compiler.ParserUtils;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.ir.untyped.*;
import org.stanford.ravel.compiler.ir.untyped.BinaryArithOp;
import org.stanford.ravel.compiler.ir.untyped.IfStatement;
import org.stanford.ravel.compiler.ir.untyped.ImmediateLoad;
import org.stanford.ravel.compiler.ir.untyped.Instruction;
import org.stanford.ravel.compiler.ir.untyped.Move;
import org.stanford.ravel.compiler.ir.untyped.UnaryArithOp;
import org.stanford.ravel.compiler.symbol.TypeSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;

import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;

/**
 * Converts tree-like UntypedIR to TypedIR, checking the type of
 * expressions and converting to control flow graph in the process
 *
 * Created by gcampagn on 1/23/17.
 */
public class TypeResolvePass implements InstructionVisitor {
    private final ControllerEventCompiler compiler;
    private final ControlFlowGraphBuilder cfgBuilder = new ControlFlowGraphBuilder();
    private final LoopTreeBuilder loopTreeBuilder = new LoopTreeBuilder();

    private TypedIR ir = new TypedIR();
    private int nextRegister = UNSET_REG;
    private TBlock currentLoopHead = null;
    private TBlock currentLoopContinuation = null;

    public TypeResolvePass(ControllerEventCompiler compiler) {
        this.compiler = compiler;
        loopTreeBuilder.addBasicBlock(cfgBuilder.getEntry());
    }

    public void declare(VariableSymbol sym) {
        Type type = sym.getType();
        if (type == PrimitiveType.ANY)
            return;
        if (sym.getRegister() == Registers.UNSET_REG)
            return;
        if (type instanceof ClassType)
            type = ((ClassType) type).getInstanceType();
        setRegisterType(sym.getRegister(), type);
    }

    public TypedIR run(UntypedIR ir) {
        this.nextRegister = ir.numUsedRegisters();
        ir.getRoot().accept(this);
        this.ir.finish(cfgBuilder, loopTreeBuilder);
        return this.ir;
    }

    private void setRegisterType(int reg, Type type) {
        ir.setRegisterType(reg, type);
    }
    private Type getRegisterType(int reg) {
        return ir.getRegisterType(reg);
    }
    private int allocateRegister(Type type) {
        assert type != PrimitiveType.VOID;

        int reg = nextRegister++;
        setRegisterType(reg, type);
        return reg;
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
            cfgBuilder.addInstruction(new TConvert(PrimitiveType.INT32, indexType, index, instr.index));
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

        cfgBuilder.addInstruction(new TArrayLoad(resultType, (ArrayType)ownerType, target, instr.source, index));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
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
            cfgBuilder.addInstruction(new TConvert(PrimitiveType.INT32, indexType, index, instr.index));
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
            cfgBuilder.addInstruction(new TConvert(targetType, sourceType, value, instr.value));
        } else {
            typeError(instr, "cannot assign expression of type " + sourceType.getName() + " to an element of array of type " + arrayType.getName());
            value = Registers.ERROR_REG;
        }
        cfgBuilder.addInstruction(new TArrayStore(targetType, arrayType, instr.object, index, value));
    }

    private Type promote(Type t1, Type t2) {
        // convert bool -> int -> double
        if (t1.equals(t2))
            return t1;
        if (t1.isAssignable(t2))
            return t1;
        if (t2.isAssignable(t1))
            return t2;

        return PrimitiveType.ERROR;
    }

    private Type adjustTypeForBinaryOp(BinaryOperation op, Type resultType) {
        if (resultType == PrimitiveType.ERROR)
            return resultType;
        if (!(resultType instanceof PrimitiveType))
            return PrimitiveType.ERROR;

        if (op.isBitwise()) {
            if (resultType == PrimitiveType.INT32)
                return resultType;
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            return PrimitiveType.ERROR;
        }
        if (op.isNumeric()) {
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            if (resultType != PrimitiveType.INT32 && resultType != PrimitiveType.DOUBLE)
                return PrimitiveType.ERROR;
            return resultType;
        }
        if (op == BinaryOperation.ADD && resultType == PrimitiveType.BOOL)
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
            cfgBuilder.addInstruction(new TConvert(resultType, srcType1, src1, instr.src1));
        } else {
            src1 = instr.src1;
        }
        int src2;
        if (!resultType.equals(srcType2)) {
            src2 = allocateRegister(resultType);
            cfgBuilder.addInstruction(new TConvert(resultType, srcType2, src2, instr.src2));
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

        cfgBuilder.addInstruction(new TBinaryArithOp(resultType, target, src1, src2, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(ComparisonOp instr) {
        Type srcType1 = getRegisterType(instr.src1);
        Type srcType2 = getRegisterType(instr.src2);

        Type opType = promote(srcType1, srcType2);
        Type resultType = PrimitiveType.BOOL;
        if (opType == PrimitiveType.ERROR) {
            typeError(instr, "invalid operand types " + srcType1.getName() + " and " + srcType2.getName() + " for comparison " + instr.op);
        }

        int src1;
        if (!opType.equals(srcType1)) {
            src1 = allocateRegister(resultType);
            cfgBuilder.addInstruction(new TConvert(opType, srcType1, src1, instr.src1));
        } else {
            src1 = instr.src1;
        }
        int src2;
        if (!opType.equals(srcType2)) {
            src2 = allocateRegister(resultType);
            cfgBuilder.addInstruction(new TConvert(opType, srcType1, src2, instr.src2));
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

        cfgBuilder.addInstruction(new TComparisonOp(opType, target, src1, src2, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(FieldLoad instr) {
        Type ownerType = getRegisterType(instr.source);
        if (!(ownerType instanceof CompoundType)) {
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

        cfgBuilder.addInstruction(new TFieldLoad(resultType, compoundType, target, instr.source, instr.field));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
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
            cfgBuilder.addInstruction(new TConvert(targetType, sourceType, value, instr.value));
        } else {
            typeError(instr, "cannot assign expression of type " + sourceType.getName() + " to a field " + ownerType.getName() + "." + instr.field + " of type " + targetType.getName());
            value = Registers.ERROR_REG;
        }
        cfgBuilder.addInstruction(new TFieldStore(targetType, compoundType, instr.object, instr.field, value));
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
                    cfgBuilder.addInstruction(new TConvert(formalType, argType, arguments[i], arg));
                }
            }
        }

        Type resultType = functionType.getReturnType();

        int target;
        Type targetType = getRegisterType(instr.target);
        if (targetType == PrimitiveType.ANY) {
            target = instr.target;
            setRegisterType(target, resultType);
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

        cfgBuilder.addInstruction(new TMethodCall(functionType, target, instr.owner, instr.method, arguments));
        if (target != instr.target && target != Registers.ERROR_REG && target != Registers.VOID_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
    }

    @Override
    public void visit(IfStatement instr) {
        Type condType = getRegisterType(instr.cond);

        int cond;
        if (condType == PrimitiveType.BOOL) {
            cond = instr.cond;
        } else if (PrimitiveType.BOOL.isAssignable(condType)) {
            cond = allocateRegister(PrimitiveType.BOOL);
            cfgBuilder.addInstruction(new TConvert(PrimitiveType.BOOL, condType, cond, instr.cond));
        } else {
            typeError(instr, "condition in if statement must be a boolean (found " + condType.getName() + ")");
            cond = Registers.ERROR_REG;
        }

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        cfgBuilder.addInstruction(new TIfStatement(cond, iftrue, iffalse));

        loopTreeBuilder.ifStatement(cond, iftrue, iffalse);

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

        cfgBuilder.addInstruction(new TImmediateLoad(literalType, target, instr.value));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, literalType, instr.target, target));
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

        if (convert)
            cfgBuilder.addInstruction(new TConvert(targetType, sourceType, instr.target, instr.source));
        else
            cfgBuilder.addInstruction(new TMove(targetType, instr.target, instr.source));
    }

    private Type adjustTypeForUnaryOp(UnaryOperation op, Type resultType) {
        if (resultType == PrimitiveType.ERROR)
            return resultType;
        if (!(resultType instanceof PrimitiveType))
            return PrimitiveType.ERROR;

        if (op.isBitwise()) {
            if (resultType == PrimitiveType.INT32)
                return resultType;
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            return PrimitiveType.ERROR;
        }
        if (op.isNumeric()) {
            if (resultType == PrimitiveType.BOOL)
                return PrimitiveType.INT32;
            if (resultType != PrimitiveType.INT32 && resultType != PrimitiveType.DOUBLE)
                return PrimitiveType.ERROR;
            return resultType;
        }

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
            cfgBuilder.addInstruction(new TConvert(resultType, srcType, src, instr.source));
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

        cfgBuilder.addInstruction(new TUnaryArithOp(resultType, target, src, instr.op));
        if (target != instr.target && target != Registers.ERROR_REG)
            cfgBuilder.addInstruction(new TConvert(targetType, resultType, instr.target, target));
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
            cfgBuilder.addInstruction(new TConvert(PrimitiveType.BOOL, condType, cond, instr.cond));
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
        cfgBuilder.addInstruction(new TIfStatement(cond, empty, breakBlock));

        loopTreeBuilder.ifStatement(cond, empty, breakBlock);
        cfgBuilder.pushBlock(empty);
        cfgBuilder.addSuccessor(loopBody);
        cfgBuilder.popBlock();
        loopTreeBuilder.elseStatement(cond);
        cfgBuilder.pushBlock(breakBlock);
        cfgBuilder.addInstruction(new TBreak());
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

        cfgBuilder.addInstruction(new TBreak());

        // code after break will be dead, but we need a block to appease CfgBuilder
        TBlock afterBreak = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(currentLoopContinuation);
        loopTreeBuilder.addBasicBlock(afterBreak);
        cfgBuilder.replaceBlock(afterBreak);
    }

    @Override
    public void visit(Continue instr) {
        if (currentLoopHead == null) {
            typeError(instr, "continue statement not inside a loop");
            return;
        }

        cfgBuilder.addInstruction(new TContinue());

        // see comment in visit(Break)
        TBlock afterContinue = cfgBuilder.newBlock();
        cfgBuilder.addSuccessor(currentLoopHead);
        loopTreeBuilder.addBasicBlock(afterContinue);
        cfgBuilder.replaceBlock(afterContinue);
    }
}
