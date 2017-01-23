package org.stanford.ravel.compiler.ir;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.ControllerCompiler;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.ir.untyped.*;
import org.stanford.ravel.compiler.ir.untyped.BinaryArithOp;
import org.stanford.ravel.compiler.ir.untyped.Block;
import org.stanford.ravel.compiler.ir.untyped.IfStatement;
import org.stanford.ravel.compiler.ir.untyped.ImmediateLoad;
import org.stanford.ravel.compiler.ir.untyped.Instruction;
import org.stanford.ravel.compiler.ir.untyped.Move;
import org.stanford.ravel.compiler.ir.untyped.UnaryArithOp;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Primitive;

import java.util.HashMap;
import java.util.Map;

import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;
import static org.stanford.ravel.compiler.ir.Registers.VOID_REG;

/**
 * Converts tree-like UntypedIR to TypedIR, checking the type of
 * expressions and converting to control flow graph in the process
 *
 * Created by gcampagn on 1/23/17.
 */
public class TypeResolvePass implements InstructionVisitor {
    private final ControllerCompiler compiler;
    private final ControlFlowGraphBuilder cfgBuilder = new ControlFlowGraphBuilder();

    private Map<Integer, Type> registerTypes = new HashMap<>();
    private int nextRegister = UNSET_REG;

    public TypeResolvePass(ControllerCompiler compiler) {
        this.compiler = compiler;
    }

    public void declare(VariableSymbol sym) {
        Type type = sym.getType();
        if (type == PrimitiveType.ANY)
            return;
        setRegisterType(sym.getRegister(), type);
    }

    public ControlFlowGraph run(UntypedIR ir) {
        this.nextRegister = ir.numUsedRegisters();
        ir.getRoot().accept(this);
        return cfgBuilder.build();
    }

    private void setRegisterType(int reg, Type type) {
        assert Registers.isNormal(reg);
        assert !registerTypes.containsKey(reg);
        registerTypes.put(reg, type);
    }
    private Type getRegisterType(int reg) {
        if (reg == Registers.VOID_REG)
            return PrimitiveType.VOID;
        if (reg == Registers.ERROR_REG)
            return PrimitiveType.ERROR;
        Type type = registerTypes.get(reg);
        if (type == null)
            return PrimitiveType.ANY;
        else
            return type;
    }
    private int allocateRegister(Type type) {
        int reg = nextRegister++;
        setRegisterType(reg, type);
        return reg;
    }

    private void typeError(Instruction instr, String message) {
        compiler.emitError(new SourceLocation(instr.definer), message);
    }

    @Override
    public void visit(ArrayLoad instr) {
        // TODO
    }

    @Override
    public void visit(ArrayStore instr) {
        // TODO
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
            cfgBuilder.addInstruction(new TConvert(resultType, srcType1, src2, instr.src2));
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
        // TODO
    }

    @Override
    public void visit(FieldStore instr) {
        // TODO
    }

    @Override
    public void visit(FunctionCall instr) {
        // TODO
    }

    @Override
    public void visit(IfStatement instr) {
        Type condType = getRegisterType(instr.cond);

        if (!PrimitiveType.BOOL.isAssignable(condType)) {
            typeError(instr, "condition in if statement must be a boolean (found " + condType.getName() + ")");
        }
        int cond;
        if (condType != PrimitiveType.BOOL) {
            cond = allocateRegister(PrimitiveType.BOOL);
            cfgBuilder.addInstruction(new TConvert(PrimitiveType.BOOL, condType, cond, instr.cond));
        } else {
            cond = instr.cond;
        }

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        cfgBuilder.addInstruction(new TIfStatement(cond, iftrue, iffalse));

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);
        instr.iftrue.accept(this);
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();
        cfgBuilder.pushBlock(iffalse);
        instr.iffalse.accept(this);
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        cfgBuilder.replaceBlock(continuation);
    }

    private Type typeFromLiteral(Object literal) {
        if (literal instanceof Boolean)
            return PrimitiveType.BOOL;
        if (literal instanceof String)
            return PrimitiveType.STR;
        if (literal instanceof Double)
            return PrimitiveType.DOUBLE;
        if (literal instanceof Integer)
            return PrimitiveType.INT32;
        throw new AssertionError("Unexpected literal " + literal);
    }

    @Override
    public void visit(ImmediateLoad instr) {
        Type literalType = typeFromLiteral(instr.value);

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

    @Override
    public void visit(SymbolLoad instr) {
        // TODO
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
        // TODO
    }
}
