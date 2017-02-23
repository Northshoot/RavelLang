package org.stanford.ravel.analysis.security;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.BinaryOperation;
import org.stanford.ravel.compiler.ir.ComparisonOperation;
import org.stanford.ravel.compiler.ir.OptimizePass;
import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.ConcreteModel;
import org.stanford.ravel.primitives.ModelField;
import org.stanford.ravel.primitives.Space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Apply the result of security analysis by transforming the IR and tagging the spaces
 * with the operations they need to perform
 *
 * Created by gcampagn on 2/21/17.
 */
public class SecurityTransformation {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private final boolean enableEncryption;
    private final boolean enableMAC;

    public SecurityTransformation(RavelCompiler driver, RavelApplication app, boolean debug, boolean disableEncryption, boolean disableMAC) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
        this.enableEncryption = !disableEncryption;
        this.enableMAC = !disableMAC;
    }

    private void addWritePrimitive(TypedIRBuilder builder, int buffer, int value, PrimitiveType type) {
        FunctionType fn = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("write_" + type.getName().toLowerCase());
        builder.add(new TMethodCall(fn, Registers.VOID_REG, buffer, fn.getFunctionName(), new int[]{value}));
    }

    private void addWriteArray(TypedIRBuilder builder, int buffer, int value, ArrayType arrayType) {
        int len = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(TIntrinsic.createArrayLength(arrayType, len, value));

        FunctionType writeLength = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("write_uint16");
        builder.add(new TMethodCall(writeLength, Registers.VOID_REG, buffer, writeLength.getFunctionName(), new int[]{len}));

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

    private TypedIR compileEncrypt(ConcreteModel model, List<SecurityOperation> ops) {
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
        for (ModelField field : model.getBaseModel().getFields()) {
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
        int buffer = builder.allocateRegister(IntrinsicTypes.GROWABLE_BYTE_ARRAY);
        builder.add(new TMethodCall((FunctionType)IntrinsicTypes.GROWABLE_BYTE_ARRAY.getMemberType("create"), buffer, Registers.VOID_REG, "create", new int[]{}));

        // write the model ID
        int modelId = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, modelId, model.getBaseModel().getId()));
        addWritePrimitive(builder, buffer, modelId, PrimitiveType.INT32);

        // write the record ID
        addWritePrimitive(builder, buffer, idxSym.getRegister(), PrimitiveType.INT32);

        if (enableEncryption) {
            // TODO do something and encrypt
        } else {
            // write each field sequentially
            for (ModelField field : model.getBaseModel().getFields()) {
                Type fieldType = field.getType();

                addWrite(builder, buffer, fields.get(field.getName()), fieldType);
            }
        }

        int byteArray = builder.allocateRegister(new ArrayType(PrimitiveType.BYTE));
        FunctionType toByteArray = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("toByteArray");
        builder.add(new TMethodCall(toByteArray, byteArray, buffer, toByteArray.getFunctionName(), new int[]{}));

        if (enableMAC) {
            int macLength = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(new TImmediateLoad(PrimitiveType.INT32, macLength, 8));

            int mac = builder.allocateRegister(new ArrayType(PrimitiveType.BYTE));
            builder.add(TIntrinsic.createArrayNew(new ArrayType(PrimitiveType.BYTE), mac, macLength));

            builder.add(new TIntrinsic(PrimitiveType.VOID, new Type[]{ new ArrayType(PrimitiveType.BYTE), new ArrayType(PrimitiveType.BYTE) },
                    Registers.VOID_REG, "compute_mac", new int[]{ byteArray, mac },
                    true, true, false));

            FunctionType writeByteArray = (FunctionType) IntrinsicTypes.GROWABLE_BYTE_ARRAY.getInstanceType().getMemberType("write_byte_array");
            builder.add(new TMethodCall(writeByteArray, Registers.VOID_REG, buffer, writeByteArray.getFunctionName(), new int[]{ mac }));

            builder.add(new TMethodCall(toByteArray, byteArray, buffer, toByteArray.getFunctionName(), new int[]{}));
        } else {
            // nothing to do
        }

        builder.add(new TMove(new ArrayType(PrimitiveType.BYTE), Registers.RETURN_REG, byteArray));
        builder.add(new TReturn());

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Encrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized encrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }

        return ir;
    }

    private int primitiveTypeSize(PrimitiveType type) {
        switch (type) {
            case VOID:
            case ANY:
            case ERROR:
                throw new AssertionError();

            case BOOL:
                return 1;
            case BYTE:
                return 1;
            case INT32:
                return 4;
            case DOUBLE:
                return 8;
            case STR:
                return -1;
            case ERROR_MSG:
                return 4;
            case TIMESTAMP:
                return 4;

            default:
                throw new AssertionError();
        }
    }

    private void buildExtractPrimitive(TypedIRBuilder builder, PrimitiveType type, int target, int data, int pos) {
        if (type == PrimitiveType.STR) {
            buildExtractString(builder, target, data, pos);
        } else {
            int size = primitiveTypeSize(type);
            assert size > 0;

            // target = extract_<type>(data, pos)
            builder.add(new TIntrinsic(type, new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32}, target, "extract_" + type.getName().toLowerCase(), new int[]{data, pos}));

            // size = ...
            // pos = pos + size
            int sizeReg = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(new TImmediateLoad(PrimitiveType.INT32, sizeReg, size));
            builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, sizeReg, BinaryOperation.ADD));
        }
    }

    private void buildExtractString(TypedIRBuilder builder, int target, int data, int pos) {
        int sizeReg = builder.allocateRegister(PrimitiveType.INT32);
        // size = extract_uint16(data, pos)
        builder.add(new TIntrinsic(PrimitiveType.INT32, new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32}, sizeReg, "extract_uint16", new int[]{data, pos}));

        // pos = pos + 2
        int twoReg = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, twoReg, 2));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, twoReg, BinaryOperation.ADD));

        // target = extract_str(data, pos, size)
        builder.add(new TIntrinsic(PrimitiveType.STR, new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32, PrimitiveType.INT32}, target, "extract_str", new int[]{data, pos, sizeReg}));

        // pos = pos + size
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, sizeReg, BinaryOperation.ADD));
    }

    private void buildExtractArray(TypedIRBuilder builder, ArrayType arrayType, int target, int data, int pos) {
        // int len;
        int len = builder.allocateRegister(PrimitiveType.INT32);
        // len = extract_uint16(data, pos)
        builder.add(new TIntrinsic(PrimitiveType.INT32, new Type[]{new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32}, len, "extract_uint16", new int[]{data, pos}));

        // pos = pos + 2
        int twoReg = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, twoReg, 2));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, twoReg, BinaryOperation.ADD));

        // target = new array[len]
        builder.add(TIntrinsic.createArrayNew(arrayType, target, len));

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

        // elem = ...
        int elem = builder.allocateRegister(arrayType.getElementType());
        buildExtract(builder, arrayType.getElementType(), elem, data, pos);

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
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void buildExtract(TypedIRBuilder builder, Type type, int target, int data, int pos) {
        if (type instanceof PrimitiveType)
            buildExtractPrimitive(builder, (PrimitiveType)type, target, data, pos);
        else if (type instanceof ArrayType)
            buildExtractArray(builder, (ArrayType)type, target, data, pos);
        else
            throw new AssertionError("Unsupported field type " + type.getName());
    }

    private TypedIR compileDecrypt(ConcreteModel model, List<SecurityOperation> ops) {
        TypedIRBuilder builder = new TypedIRBuilder();

        // this code ends up in the "from network" constructor of Model.Record, so
        // declare "this" as a variable of record type
        // we cannot use regular variables because we're setting them and that
        // would confuse the optimizations
        int firstReg = Registers.FIRST_GP_REG;

        ModelType.RecordType recordType = model.getBaseModel().getType().getRecordType();

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

        builder.setNextRegister(firstReg);
        builder.setRegisterType(Registers.RETURN_REG, PrimitiveType.VOID);

        int thisReg = thisSym.getRegister();
        int data = dataSym.getRegister();

        if (enableMAC) {
            int macLength = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(new TImmediateLoad(PrimitiveType.INT32, macLength, 8));

            int dataLength = builder.allocateRegister(PrimitiveType.INT32);
            builder.add(TIntrinsic.createArrayLength((ArrayType)dataSym.getType(), dataLength, dataSym.getRegister()));
            builder.add(new TBinaryArithOp(PrimitiveType.INT32, dataLength, dataLength, macLength, BinaryOperation.SUB));

            builder.add(new TIntrinsic(PrimitiveType.VOID, new Type[]{ new ArrayType(PrimitiveType.BYTE), PrimitiveType.INT32, PrimitiveType.INT32 },
                    Registers.VOID_REG, "verify_mac", new int[]{ data, dataLength, macLength },
                    false, true, true));
        } else {
            // nothing to do
        }

        // pos = 0
        int pos = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, pos, 0));

        // skip model id:
        // pos += 4
        int four = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, four, 4));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, pos, pos, four, BinaryOperation.ADD));

        // extract the record id
        int idx = builder.allocateRegister(PrimitiveType.INT32);
        buildExtractPrimitive(builder, PrimitiveType.INT32, idx, data, pos);
        // this is lying a little bit because __idx is not a field of recordType,
        // but as long as the validate pass doesn't check for that we're good
        builder.add(new TFieldStore(PrimitiveType.INT32, recordType, thisReg, "__idx", idx));

        if (enableEncryption) {
            // TODO do something and decrypt
        } else {
            for (ModelField field : model.getBaseModel().getFields()) {
                Type fieldType = field.getType();

                int value = builder.allocateRegister(fieldType);
                buildExtract(builder, fieldType, value, data, pos);
                builder.add(new TFieldStore(fieldType, recordType, thisReg, field.getName(), value));
            }
        }

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Decrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized decrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }

        return ir;
    }

    public void run() {
        for (Space space : app.getSpaces()) {
            for (ConcreteModel im : space.getModels()) {
                im.setReceiveCode(compileDecrypt(im, im.getSecurityOperations(true)));
                im.setSendCode(compileEncrypt(im, im.getSecurityOperations(false)));
            }
        }
    }
}
