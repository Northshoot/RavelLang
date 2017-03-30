package edu.stanford.ravel;

import edu.stanford.ravel.compiler.ir.*;
import edu.stanford.ravel.compiler.ir.typed.*;
import edu.stanford.ravel.compiler.SourceLocation;
import edu.stanford.ravel.compiler.symbol.*;
import edu.stanford.ravel.compiler.types.*;
import edu.stanford.ravel.error.FatalCompilerErrorException;
import edu.stanford.ravel.primitives.ModelField;
import edu.stanford.ravel.compiler.scope.Scope;
import edu.stanford.ravel.primitives.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Compile a ModelSymbol into the resulting primitive Model.
 *
 * Mostly it boils down to splitting the symbol declarations in the right
 * places, and never fails really.
 *
 * Created by lauril on 1/20/17.
 */
public class ModelCompiler {
    private final RavelCompiler driver;
    private final boolean debug;

    public ModelCompiler(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    private void addWritePrimitive(TypedIRBuilder builder, int buffer, int value, PrimitiveType type) {
        FunctionType fn = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("write_" + type.getName().toLowerCase());
        builder.add(new TMethodCall(fn, Registers.VOID_REG, buffer, fn.getFunctionName(), new int[]{value}));
    }

    private void addWriteArray(TypedIRBuilder builder, int buffer, int value, ArrayType arrayType) {
        int len = builder.allocateRegister(PrimitiveType.INT32);

        if (arrayType.isKnownBound()) {
            builder.add(new TImmediateLoad(PrimitiveType.INT32, len, arrayType.getBound()));
        } else {
            builder.add(IntrinsicFactory.createArrayLength(arrayType, len, value));

            FunctionType writeLength = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("write_uint16");
            builder.add(new TMethodCall(writeLength, Registers.VOID_REG, buffer, writeLength.getFunctionName(), new int[]{len}));
        }

        // int i;
        int i = builder.allocateRegister(PrimitiveType.INT32);
        // i = 0;
        builder.add(new TImmediateLoad(PrimitiveType.INT32, i, 0));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // while(true) {
        TBlock loopHead = cfgBuilder.newBlock();
        TBlock continuation = cfgBuilder.newBlock();

        loopTreeBuilder.beginLoop(loopHead);
        cfgBuilder.addSuccessor(loopHead);

        cfgBuilder.pushBlock(loopHead);

        // bool cond;
        int cond = builder.allocateRegister(PrimitiveType.BOOL);
        // cond = i < len
        builder.add(new TComparisonOp(PrimitiveType.INT32, cond, i, len, ComparisonOperation.LT));

        // if (cond) {}
        // else { break }
        TBlock empty = cfgBuilder.newBlock();
        TBlock breakBlock = cfgBuilder.newBlock();
        TBlock loopBody = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(empty);
        cfgBuilder.addSuccessor(breakBlock);
        TIfStatement ifStatement = new TIfStatement(cond, empty, breakBlock);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, empty, breakBlock);
        cfgBuilder.pushBlock(empty);
        cfgBuilder.addSuccessor(loopBody);
        cfgBuilder.popBlock();
        loopTreeBuilder.elseStatement(cond);
        cfgBuilder.pushBlock(breakBlock);
        builder.add(new TBreak());
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();
        loopTreeBuilder.endIfStatement(cond);

        cfgBuilder.replaceBlock(loopBody);
        loopTreeBuilder.addBasicBlock(loopBody);

        // elem = value[i]
        int elem = builder.allocateRegister(arrayType.getElementType());
        builder.add(new TArrayLoad(arrayType.getElementType(), arrayType, elem, value, i));

        // write(elem)
        addWrite(builder, buffer, elem, arrayType.getElementType());

        // i++
        int oneReg = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, oneReg, 1));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, i, i, oneReg, BinaryOperation.ADD));

        // } end while
        cfgBuilder.addSuccessor(loopHead);
        cfgBuilder.popBlock();
        loopTreeBuilder.endLoop();
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void addWrite(TypedIRBuilder builder, int buffer, int value, Type type) {
        if (type instanceof PrimitiveType)
            addWritePrimitive(builder, buffer, value, (PrimitiveType)type);
        else if (type instanceof ArrayType)
            addWriteArray(builder, buffer, value, (ArrayType) type);
        else
            throw new AssertionError("Unsupported field type " + type.getName());
    }

    private TypedIR compileSend(Model model) {
        TypedIRBuilder builder = new TypedIRBuilder();

        // this code ends up in the "to bytes" method of Model.Record, so
        // declare class level variables for the model fields
        int firstReg = Registers.FIRST_GP_REG;
        VariableSymbol idxSym = new VariableSymbol("__idx");
        idxSym.setRegister(firstReg++);
        idxSym.setType(PrimitiveType.INT32);
        idxSym.setWritable(false);
        builder.declareParameter(idxSym, true);

        Map<String, Integer> fields = new HashMap<>();
        for (ModelField field : model.getFields()) {
            VariableSymbol sym = new VariableSymbol(field.getName());
            sym.setRegister(firstReg++);
            sym.setType(field.getType());
            sym.setWritable(false);
            builder.declareParameter(sym, true);
            fields.put(field.getName(), sym.getRegister());
        }

        builder.setNextRegister(firstReg);
        builder.setRegisterType(Registers.RETURN_REG, new ArrayType(PrimitiveType.BYTE));

        // create a buffer to write into
        int buffer = builder.allocateRegister(IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType());
        builder.add(new TMethodCall((FunctionType)IntrinsicTypes.GROWABLE_BYTE_ARRAY.getMemberType("create"), buffer, Registers.VOID_REG, "create", new int[]{}));

        // write each field sequentially
        for (ModelField field : model.getFields()) {
            Type fieldType = field.getType();

            addWrite(builder, buffer, fields.get(field.getName()), fieldType);
        }

        int byteArray = builder.allocateRegister(new ArrayType(PrimitiveType.BYTE));
        FunctionType toByteArray = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("toByteArray");
        builder.add(new TMethodCall(toByteArray, byteArray, buffer, toByteArray.getFunctionName(), new int[]{}));

        builder.add(new TMove(new ArrayType(PrimitiveType.BYTE), Registers.RETURN_REG, byteArray));
        builder.add(new TReturn());

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Serialization IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized serialization IR for " + model);
            System.out.println(ir.getLoopTree());
        }

        return ir;
    }

    private void buildExtractPrimitive(TypedIRBuilder builder, PrimitiveType type, int target, int data, int pos, int isEncrypted) {
        if (type == PrimitiveType.STR) {
            buildExtractString(builder, target, data, pos, isEncrypted);
        } else {
            int size = type.getSerializedSize();
            assert size > 0;

            // target = extract_<type>(data, pos)
            if (builder.isParameter(target)) {
                int tmp = builder.allocateRegister(type);
                builder.add(IntrinsicFactory.createExtractPrimitive(type, tmp, data, pos));
                builder.add(IntrinsicFactory.createParameterSet(type, target, tmp));
            } else {
                builder.add(IntrinsicFactory.createExtractPrimitive(type, target, data, pos));
            }

            // size = ...
            // pos = pos + size
            int sizeReg = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(new TImmediateLoad(PrimitiveType.INT32, sizeReg, size));
            builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, sizeReg, BinaryOperation.ADD));
        }
    }

    private void buildExtractString(TypedIRBuilder builder, int target, int data, int pos, int isEncrypted) {
        int sizeReg = builder.allocateRegister(PrimitiveType.INT32);

        // if (is_encrypted) {
        // size = 0
        // } else {
        // size = extract_uint16(data, pos)
        //

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(isEncrypted, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, sizeReg, 0));

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(isEncrypted);
        cfgBuilder.pushBlock(iffalse);

        builder.add(IntrinsicFactory.createExtractUInt16(sizeReg, data, pos));

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(isEncrypted);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);

        // pos = pos + 2
        int twoReg = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, twoReg, 2));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, twoReg, BinaryOperation.ADD));

        // target = extract_str(data, pos, size)
        if (builder.isParameter(target)) {
            int tmp = builder.allocateRegister(PrimitiveType.STR);
            builder.add(IntrinsicFactory.createExtractString(tmp, data, pos, sizeReg));
            builder.add(IntrinsicFactory.createParameterSet(PrimitiveType.STR, target, tmp));
        } else {
            builder.add(IntrinsicFactory.createExtractString(target, data, pos, sizeReg));
        }

        // pos = pos + size
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, sizeReg, BinaryOperation.ADD));
    }

    private void buildExtractArray(TypedIRBuilder builder, ArrayType arrayType, int target, int data, int pos, int isEncrypted) {
        // int len;
        int len = builder.allocateRegister(PrimitiveType.INT32);

        // if (is_encrypted) {
        // len = 0
        // } else {
        // len = extract_uint16(data, pos)
        //

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(isEncrypted, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, len, 0));

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(isEncrypted);
        cfgBuilder.pushBlock(iffalse);

        if (arrayType.isKnownBound()) {
            builder.add(new TImmediateLoad(PrimitiveType.INT32, len, arrayType.getBound()));
        } else {
            builder.add(IntrinsicFactory.createExtractUInt16(len, data, pos));
        }

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(isEncrypted);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);

        if (!arrayType.isKnownBound()) {
            // pos = pos + 2
            int twoReg = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(new TImmediateLoad(PrimitiveType.INT32, twoReg, 2));
            builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, twoReg, BinaryOperation.ADD));

            // target = new array[len]
            if (builder.isParameter(target)) {
                int tmp = builder.allocateRegister(arrayType);
                builder.add(IntrinsicFactory.createArrayNew(arrayType, tmp, len));
                builder.add(IntrinsicFactory.createParameterSet(arrayType, target, tmp));
            } else {
                builder.add(IntrinsicFactory.createArrayNew(arrayType, target, len));
            }
        }

        // int i;
        int i = builder.allocateRegister(PrimitiveType.INT32);
        // i = 0;
        builder.add(new TImmediateLoad(PrimitiveType.INT32, i, 0));

        // while(true) {
        TBlock loopHead = cfgBuilder.newBlock();
        TBlock loopContinuation = cfgBuilder.newBlock();

        loopTreeBuilder.beginLoop(loopHead);
        cfgBuilder.addSuccessor(loopHead);

        cfgBuilder.pushBlock(loopHead);

        // bool cond;
        int cond = builder.allocateRegister(PrimitiveType.BOOL);
        // cond = i < len
        builder.add(new TComparisonOp(PrimitiveType.INT32, cond, i, len, ComparisonOperation.LT));

        // if (cond) {}
        // else { break }
        TBlock empty = cfgBuilder.newBlock();
        TBlock breakBlock = cfgBuilder.newBlock();
        TBlock loopBody = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(empty);
        cfgBuilder.addSuccessor(breakBlock);
        TIfStatement loopIfStatement = new TIfStatement(cond, empty, breakBlock);
        builder.add(loopIfStatement);

        loopTreeBuilder.ifStatement(loopIfStatement, empty, breakBlock);
        cfgBuilder.pushBlock(empty);
        cfgBuilder.addSuccessor(loopBody);
        cfgBuilder.popBlock();
        loopTreeBuilder.elseStatement(cond);
        cfgBuilder.pushBlock(breakBlock);
        builder.add(new TBreak());
        cfgBuilder.addSuccessor(loopContinuation);
        cfgBuilder.popBlock();
        loopTreeBuilder.endIfStatement(cond);

        cfgBuilder.replaceBlock(loopBody);
        loopTreeBuilder.addBasicBlock(loopBody);

        // elem = ...
        int elem = builder.allocateRegister(arrayType.getElementType());
        buildExtract(builder, arrayType.getElementType(), elem, data, pos, isEncrypted);

        // target[i] = elem
        builder.add(new TArrayStore(arrayType.getElementType(), arrayType, target, i, elem));

        // i++
        int oneReg = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, oneReg, 1));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, i, i, oneReg, BinaryOperation.ADD));

        // } end while
        cfgBuilder.addSuccessor(loopHead);
        cfgBuilder.popBlock();
        loopTreeBuilder.endLoop();
        loopTreeBuilder.addBasicBlock(loopContinuation);
        cfgBuilder.replaceBlock(loopContinuation);
    }

    private void buildExtract(TypedIRBuilder builder, Type type, int target, int data, int pos, int isEncrypted) {
        if (type instanceof PrimitiveType)
            buildExtractPrimitive(builder, (PrimitiveType)type, target, data, pos, isEncrypted);
        else if (type instanceof ArrayType)
            buildExtractArray(builder, (ArrayType)type, target, data, pos, isEncrypted);
        else
            throw new AssertionError("Unsupported field type " + type.getName());
    }

    private TypedIR compileReceive(Model model) {
        TypedIRBuilder builder = new TypedIRBuilder();

        // this code ends up in the "from network" constructor of Model.Record, so
        // declare "this" as a variable of record type
        // we cannot use regular variables because we're setting them and that
        // would confuse the optimizations
        int firstReg = Registers.FIRST_GP_REG;

        ModelType.RecordType recordType = model.getType().getRecordType();

        VariableSymbol thisSym = new VariableSymbol("this");
        thisSym.setRegister(firstReg++);
        thisSym.setWritable(false);
        thisSym.setType(recordType);
        builder.declareParameter(thisSym, false);

        VariableSymbol dataSym = new VariableSymbol("data");
        dataSym.setType(new ArrayType(PrimitiveType.BYTE));
        dataSym.setWritable(false);
        dataSym.setRegister(firstReg++);
        builder.declareParameter(dataSym, false);

        VariableSymbol isEncryptedSym = new VariableSymbol("is_encrypted");
        isEncryptedSym.setType(PrimitiveType.BOOL);
        isEncryptedSym.setWritable(false);
        isEncryptedSym.setRegister(firstReg++);
        builder.declareParameter(isEncryptedSym, false);

        Map<String, VariableSymbol> fieldSyms = new HashMap<>();

        for (ModelField field : model.getFields()) {
            VariableSymbol fieldVarSym = new VariableSymbol(field.getName());
            fieldVarSym.setType(field.getType());
            fieldVarSym.setWritable(true);
            fieldVarSym.setRegister(firstReg++);
            builder.declareParameter(fieldVarSym, true);
            fieldSyms.put(field.getName(), fieldVarSym);
        }

        builder.setNextRegister(firstReg);
        builder.setRegisterType(Registers.RETURN_REG, PrimitiveType.VOID);

        int thisReg = thisSym.getRegister();
        int data = dataSym.getRegister();

        // pos = 0
        int pos = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, pos, 0));

        for (ModelField field : model.getFields()) {
            Type fieldType = field.getType();

            int value = fieldSyms.get(field.getName()).getRegister();
            buildExtract(builder, fieldType, value, data, pos, isEncryptedSym.getRegister());
            //builder.add(new TFieldStore(fieldType, recordType, thisReg, field.getName(), value));
        }

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Deserialization IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized deserialization IR for " + model);
            System.out.println(ir.getLoopTree());
        }

        return ir;
    }

    public Model compile(ModelSymbol ms) throws FatalCompilerErrorException {
        // make concrete model
        String name = ms.getName();
        Model m = new Model(name, ms);

        for (Symbol s : ms.getSymbols()) {
            if (s instanceof VariableSymbol)
                m.addParameter((VariableSymbol)s);
        }

        Scope propScope = ms.getNestedScope("properties");

        for (Symbol s : propScope.getAllSymbols()) {
            switch (s.getName()) {
                case "records":
                case "durable":
                case "reliable":
                case "flow":
                    break;
                default:
                    driver.emitWarning(new SourceLocation(s.getDefNode()), "ignored model property " + s.getName());
            }

            if (s.getName().equals("flow") && !(s instanceof FlowSymbol))
                driver.emitError(new SourceLocation(s.getDefNode()), "flow property must be in flow syntax (space1 -> space2 -> ... spaceN for streaming models, space1, space2, ... spaceN for replicated models)");

            if (s instanceof ConstantSymbol)
                m.setConstantProperty(s.getName(), ((ConstantSymbol) s).getValue());
            else if (s instanceof ReferenceSymbol)
                m.setReferenceProperty(s.getName(), ((ReferenceSymbol) s).getValue());
            else if (s instanceof FlowSymbol)
                ; // let FlowAnalysis deal with that
            else
                throw new AssertionError();
        }

        // create fields for the schema
        Scope schemaScope = ms.getNestedScope("schema");
        Collection<Symbol> schema = schemaScope.getAllSymbols();
        for (Symbol s : schemaScope.getSymbols()) {
            FieldSymbol fs = (FieldSymbol)s;
            m.addField(fs);
        }
        //end field constructions

        m.packFields();

        m.setSendCode(compileSend(m));
        m.setReceiveCode(compileReceive(m));

        if (debug)
            System.out.println(m);

        return m;
    }

    public void postAnalysis(Model model) throws FatalCompilerErrorException {
        LowerIRPass pass = new LowerIRPass(driver, debug);

        pass.run(model.getReceiveCode());
        pass.run(model.getSendCode());
    }
}
