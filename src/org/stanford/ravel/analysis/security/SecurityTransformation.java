package org.stanford.ravel.analysis.security;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.*;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.IntrinsicTypes;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.SystemType;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.ConcreteModel;
import org.stanford.ravel.primitives.Space;

import java.util.List;

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
    private final boolean recordLevelEncryption;

    private final SecurityMechanism mechanism = new SecurityMechanism();

    public SecurityTransformation(RavelCompiler driver, RavelApplication app, boolean debug, boolean enableEncryption, boolean enableMAC, boolean recordLevelEncryption) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
        this.enableEncryption = enableEncryption;
        this.enableMAC = enableMAC;
        this.recordLevelEncryption = recordLevelEncryption;
    }

    private TypedIR compileEncrypt(ConcreteModel im) throws FatalCompilerErrorException {
        TypedIRBuilder builder = new TypedIRBuilder();
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        // this code ends up in "marshall" method of Model
        // declare "record" of record type, "data" of byte array type, and "endpoint" of Endpoint type
        int firstReg = Registers.FIRST_GP_REG;

        VariableSymbol dispatcherSym = new VariableSymbol("dispatcher");
        dispatcherSym.setRegister(firstReg++);
        dispatcherSym.setType(SystemType.INSTANCE.getInstanceType());
        dispatcherSym.setWritable(false);
        builder.declareParameter(dispatcherSym, true);

        VariableSymbol recordSym = new VariableSymbol("record");
        recordSym.setRegister(firstReg++);
        recordSym.setType(im.getBaseModel().getType().getRecordType());
        recordSym.setWritable(false);
        builder.declareParameter(recordSym, false);

        VariableSymbol dataSym = new VariableSymbol("data");
        dataSym.setRegister(firstReg++);
        dataSym.setType(byteArray);
        dataSym.setWritable(false);
        builder.declareParameter(dataSym, false);

        VariableSymbol endpointSym = new VariableSymbol("endpoint");
        endpointSym.setRegister(firstReg++);
        endpointSym.setType(IntrinsicTypes.ENDPOINT);
        endpointSym.setWritable(false);
        builder.declareParameter(endpointSym, false);

        builder.setNextRegister(firstReg);
        builder.setRegisterType(Registers.RETURN_REG, byteArray);

        int endpointname = builder.allocateRegister(PrimitiveType.INT32);
        // endpointname = endpoint.getId()
        builder.add(IntrinsicFactory.createEndpointGetId(endpointname, endpointSym.getRegister()));

        int maclength = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, maclength, 0));

        List<SecurityOperation> macs = im.getSecurityOperations(SecurityPrimitive.APPLY_MAC);
        List<SecurityOperation> encryptions = im.getSecurityOperations(SecurityPrimitive.ENCRYPT);

        boolean doMAC = enableMAC && macs.size() > 0;
        boolean doEncrypt = enableEncryption && encryptions.size() > 0;

        if (doMAC) {
            for (SecurityOperation op : macs) {
                prepareOneMac(builder, op, maclength, endpointname);
            }
        }

        int buffer = builder.allocateRegister(byteArray);
        int dataSize = builder.allocateRegister(PrimitiveType.INT32);
        int encryptedSize = builder.allocateRegister(PrimitiveType.INT32);
        int bufferSize = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(IntrinsicFactory.createArrayLength(byteArray, dataSize, dataSym.getRegister()));
        builder.add(new TMove(PrimitiveType.INT32, encryptedSize, dataSize));

        int ivSize = builder.allocateRegister(PrimitiveType.INT32);

        if (doEncrypt) {
            if (mechanism.mustAlignToBlockSize()) {
                // round data to a multiple of block size
                // From Hacker's delight
                // dataSize = (dataSize + blockSize - 1) & -blockSize

                int blockSize = builder.allocateRegister(PrimitiveType.INT32);
                builder.add(new TImmediateLoad(PrimitiveType.INT32, blockSize, mechanism.getEncryptionBlockSize()));

                int one = builder.allocateRegister(PrimitiveType.INT32);
                builder.add(new TImmediateLoad(PrimitiveType.INT32, one, 1));

                int negBlockSize = builder.allocateRegister(PrimitiveType.INT32);
                builder.add(new TUnaryArithOp(PrimitiveType.INT32, negBlockSize, blockSize, UnaryOperation.NEG));

                builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, encryptedSize, blockSize, BinaryOperation.ADD));
                builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, encryptedSize, one, BinaryOperation.SUB));
                builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, encryptedSize, negBlockSize, BinaryOperation.AND));
            }

            // finally add the IV size
            builder.add(new TImmediateLoad(PrimitiveType.INT32, ivSize, mechanism.getEncryptionIVSize()));
            builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, encryptedSize, ivSize, BinaryOperation.ADD));
        } else {
            builder.add(new TImmediateLoad(PrimitiveType.INT32, ivSize, 0));
        }

        int reservedSize = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, reservedSize, 3));

        builder.add(new TBinaryArithOp(PrimitiveType.INT32, bufferSize, encryptedSize, maclength, BinaryOperation.ADD));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, bufferSize, bufferSize, reservedSize, BinaryOperation.ADD));
        builder.add(IntrinsicFactory.createArrayNew(byteArray, buffer, bufferSize));

        // write the model ID
        int modelId = builder.allocateRegister(PrimitiveType.BYTE);
        builder.add(new TImmediateLoad(PrimitiveType.BYTE, modelId, (byte)im.getBaseModel().getId()));
        int zero = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, zero, 0));
        builder.add(IntrinsicFactory.createWritePrimitive(PrimitiveType.BYTE, buffer, zero, modelId));

        // write the record ID
        int recordId = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(IntrinsicFactory.createReadRecordId(im.getBaseModel().getType().getRecordType(), recordId, recordSym.getRegister()));
        int recordIdPos = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, recordIdPos, 1));
        builder.add(IntrinsicFactory.createWriteUInt16(buffer, recordIdPos, recordId));

        int encryptedStart = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TMove(PrimitiveType.INT32, encryptedStart, reservedSize));

        // copy the data to the buffer
        int dataStart = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, dataStart, reservedSize, ivSize, BinaryOperation.ADD));
        builder.add(IntrinsicFactory.createArrayCopy(byteArray, buffer, dataSym.getRegister(), dataStart, zero, dataSize));

        if (doEncrypt) {
            // fill the IV with random values
            builder.add(IntrinsicFactory.createArrayFillRandom(buffer, encryptedStart, ivSize));

            // do the actual encryption
            for (SecurityOperation op : encryptions)
                applyOneEncryption(builder, op, dispatcherSym.getRegister(), buffer, encryptedStart, encryptedSize, endpointname);
        }

        int endOfData = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, endOfData, reservedSize, encryptedSize, BinaryOperation.ADD));

        if (doMAC) {
            for (SecurityOperation op : macs) {
                applyOneMac(builder, op, dispatcherSym.getRegister(), buffer, endOfData, endpointname);
            }
        }

        builder.add(new TMove(byteArray, Registers.RETURN_REG, buffer));

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Encrypt IR for " + im);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized encrypt IR for " + im);
            System.out.println(ir.getLoopTree());
        }

        LowerIRPass lower = new LowerIRPass(driver, false);
        lower.run(ir);

        return ir;
    }

    private void applyOneEncryption(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int offset, int encryptedSize, int endpointname) {
        // the overall code looks something like
        //
        // if (endpoint.getId() == ...) {
        //   key = ...
        //   encrypt(buffer, key);
        // }

        int comparename = builder.allocateRegister(PrimitiveType.INT32);

        // comparename = ...
        builder.add(new TImmediateLoad(PrimitiveType.INT32, comparename, op.getTarget().getId()));

        // comparison = endpointname == comparename
        int comparison = builder.allocateRegister(PrimitiveType.BOOL);
        builder.add(new TComparisonOp(PrimitiveType.INT32, comparison, endpointname, comparename, ComparisonOperation.EQUAL));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(comparison, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        doApplyOneEncryption(builder, op, dispatcher, buffer, offset, encryptedSize);

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(comparison);
        cfgBuilder.pushBlock(iffalse);
        // nothing to do otherwise
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(comparison);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void doApplyOneEncryption(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int offset, int encryptedSize) {
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        int key = builder.allocateRegister(IntrinsicTypes.KEY);
        int keyId = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, keyId, op.getKey().getKeyId()));
        builder.add(IntrinsicFactory.createLoadKey(key, dispatcher, keyId));

        builder.add(IntrinsicFactory.createEncrypt(buffer, offset, encryptedSize, key));
    }

    private void applyOneDecryption(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int bufferStart, int bufferEnd, int endpointname, int isEncrypted) {
        // the overall code looks something like
        //
        // if (endpoint.getId() == ...) {
        //   key = ...
        //   encrypt(buffer, key);
        // }

        int comparename = builder.allocateRegister(PrimitiveType.INT32);

        // comparename = ...
        builder.add(new TImmediateLoad(PrimitiveType.INT32, comparename, op.getTarget().getId()));

        // comparison = endpointname == comparename
        int comparison = builder.allocateRegister(PrimitiveType.BOOL);
        builder.add(new TComparisonOp(PrimitiveType.INT32, comparison, endpointname, comparename, ComparisonOperation.EQUAL));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(comparison, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        doApplyOneDecryption(builder, op, dispatcher, buffer, bufferStart, bufferEnd);
        builder.add(new TImmediateLoad(PrimitiveType.BOOL, isEncrypted, false));

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(comparison);
        cfgBuilder.pushBlock(iffalse);
        // nothing to do otherwise
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(comparison);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void doApplyOneDecryption(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int bufferStart, int bufferEnd) {
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        int key = builder.allocateRegister(IntrinsicTypes.KEY);
        int keyId = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, keyId, op.getKey().getKeyId()));
        builder.add(IntrinsicFactory.createLoadKey(key, dispatcher, keyId));

        int encryptedSize = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, bufferEnd, bufferStart, BinaryOperation.SUB));
        builder.add(IntrinsicFactory.createDecrypt(buffer, bufferStart, encryptedSize, key));
    }

    private void prepareOneMac(TypedIRBuilder builder, SecurityOperation op, int maclength, int endpointname) {
        // the overall code looks something like
        //
        // if (endpoint.getId() == ...) {
        //   maclength = ...
        // }

        int comparename = builder.allocateRegister(PrimitiveType.INT32);

        // comparename = ...
        builder.add(new TImmediateLoad(PrimitiveType.INT32, comparename, op.getTarget().getId()));

        // comparison = endpointname == comparename
        int comparison = builder.allocateRegister(PrimitiveType.BOOL);
        builder.add(new TComparisonOp(PrimitiveType.INT32, comparison, endpointname, comparename, ComparisonOperation.EQUAL));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(comparison, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        doPrepareOneMac(builder, op, maclength);

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(comparison);
        cfgBuilder.pushBlock(iffalse);
        // nothing to do otherwise
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(comparison);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void doPrepareOneMac(TypedIRBuilder builder, SecurityOperation op, int maclength) {
        int macSize = mechanism.getMACSize();
        int numMACs = op.getFlow().getNumMACs();

        builder.add(new TImmediateLoad(PrimitiveType.INT32, maclength, macSize * numMACs));
    }

    private void applyOneMac(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int endOfData, int endpointname) {
        // the overall code looks something like
        //
        // if (endpoint.getId() == ...) {
        //   key = load_key(...);
        //   macOffset = encryptedSize + ...;
        //   apply_mac(buffer, encryptedSize, macOffset);
        // }

        int comparename = builder.allocateRegister(PrimitiveType.INT32);

        // comparename = ...
        builder.add(new TImmediateLoad(PrimitiveType.INT32, comparename, op.getTarget().getId()));

        // comparison = endpointname == comparename
        int comparison = builder.allocateRegister(PrimitiveType.BOOL);
        builder.add(new TComparisonOp(PrimitiveType.INT32, comparison, endpointname, comparename, ComparisonOperation.EQUAL));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(comparison, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        doApplyOneMac(builder, op, dispatcher, buffer, endOfData);

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(comparison);
        cfgBuilder.pushBlock(iffalse);
        // nothing to do otherwise
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(comparison);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void doApplyOneMac(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int buffer, int endOfData) {
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        int key = builder.allocateRegister(IntrinsicTypes.KEY);
        int keyId = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, keyId, op.getKey().getKeyId()));
        builder.add(IntrinsicFactory.createLoadKey(key, dispatcher, keyId));

        int macSize = mechanism.getMACSize();
        int ourMAC = op.getOffset();

        // outMACOffset = encryptedSize + ourMAC * macSize
        int ourMACOffset = builder.allocateRegister(PrimitiveType.INT32);
        int addend = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, addend, macSize * ourMAC));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, ourMACOffset, endOfData, addend, BinaryOperation.ADD));

        builder.add(IntrinsicFactory.createApplyMAC(buffer, endOfData, ourMACOffset, key));
    }

    private TypedIR compileDecrypt(ConcreteModel im) throws FatalCompilerErrorException {
        TypedIRBuilder builder = new TypedIRBuilder();
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        // this code ends up in "unmarshall" method of Model
        // declare the return value of byte array type, "data" of byte array type, and "endpoint" of Endpoint type
        int firstReg = Registers.FIRST_GP_REG;

        VariableSymbol dispatcherSym = new VariableSymbol("dispatcher");
        dispatcherSym.setRegister(firstReg++);
        dispatcherSym.setType(SystemType.INSTANCE.getInstanceType());
        dispatcherSym.setWritable(false);
        builder.declareParameter(dispatcherSym, true);

        VariableSymbol dataSym = new VariableSymbol("data");
        dataSym.setRegister(firstReg++);
        dataSym.setType(byteArray);
        dataSym.setWritable(false);
        builder.declareParameter(dataSym, false);

        VariableSymbol lengthSym = new VariableSymbol("length");
        lengthSym.setRegister(firstReg++);
        lengthSym.setType(PrimitiveType.INT32);
        lengthSym.setWritable(false);
        builder.declareParameter(lengthSym, false);

        VariableSymbol decryptedSym = new VariableSymbol("decrypted");
        decryptedSym.setRegister(firstReg++);
        decryptedSym.setType(byteArray);
        decryptedSym.setWritable(false);
        builder.declareParameter(decryptedSym, false);

        VariableSymbol endpointSym = new VariableSymbol("endpoint");
        endpointSym.setRegister(firstReg++);
        endpointSym.setType(IntrinsicTypes.ENDPOINT.getInstanceType());
        endpointSym.setWritable(false);
        builder.declareParameter(endpointSym, false);

        VariableSymbol isEncryptedSym = new VariableSymbol("is_encrypted");
        isEncryptedSym.setRegister(firstReg++);
        isEncryptedSym.setType(PrimitiveType.BOOL);
        isEncryptedSym.setWritable(true);
        builder.declareParameter(isEncryptedSym, false);

        builder.setNextRegister(firstReg);
        builder.setRegisterType(Registers.RETURN_REG, PrimitiveType.VOID);

        int endpointname = builder.allocateRegister(PrimitiveType.INT32);
        // endpointname = endpoint.getName()
        builder.add(IntrinsicFactory.createEndpointGetId(endpointname, endpointSym.getRegister()));

        int endOfData = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TMove(PrimitiveType.INT32, endOfData, lengthSym.getRegister()));

        List<SecurityOperation> macs = im.getSecurityOperations(SecurityPrimitive.VERIFY_MAC);
        List<SecurityOperation> decryptions = im.getSecurityOperations(SecurityPrimitive.DECRYPT);

        if (enableMAC) {
            for (SecurityOperation op : macs) {
                verifyOneMac(builder, op, dispatcherSym.getRegister(), dataSym.getRegister(), endOfData, endpointname);
            }
        }

        // skip the model ID and record ID
        int encryptedStart = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, encryptedStart, 3));

        int decrypted = decryptedSym.getRegister();
        int encryptedSize = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, encryptedSize, endOfData, encryptedStart, BinaryOperation.SUB));
        builder.add(IntrinsicFactory.createArrayNew(byteArray, decrypted, encryptedSize));

        // copy the data to the decrypted buffer
        int zero = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, zero, 0));
        builder.add(IntrinsicFactory.createArrayCopy(byteArray, decrypted, dataSym.getRegister(), zero, encryptedStart, encryptedSize));

        int isEncrypted = builder.allocateRegister(PrimitiveType.BOOL);

        builder.add(new TImmediateLoad(PrimitiveType.BOOL, isEncrypted, enableEncryption));
        for (SecurityOperation op : decryptions) {
            applyOneDecryption(builder, op, dispatcherSym.getRegister(), decrypted, zero, encryptedSize, endpointname, isEncrypted);
        }

        builder.add(IntrinsicFactory.createParameterSet(PrimitiveType.BOOL, isEncryptedSym.getRegister(), isEncrypted));

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Decrypt IR for " + im);
            System.out.println(ir.getLoopTree());
        }
        ValidateIR.validate(ir);
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();
        if (debug) {
            System.out.println("Optimized decrypt IR for " + im);
            System.out.println(ir.getLoopTree());
        }

        LowerIRPass lower = new LowerIRPass(driver, false);
        lower.run(ir);

        return ir;
    }

    private void verifyOneMac(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int data, int endOfData, int endpointname) {
        // the overall code looks something like
        //
        // if (endpoint.getId() == ...) {
        //   key = load_key(...);
        //   verify_mac(data, key);
        // }

        int comparename = builder.allocateRegister(PrimitiveType.INT32);

        // comparename = ...
        builder.add(new TImmediateLoad(PrimitiveType.INT32, comparename, op.getTarget().getId()));

        // comparison = endpointname == comparename
        int comparison = builder.allocateRegister(PrimitiveType.BOOL);
        builder.add(new TComparisonOp(PrimitiveType.INT32, comparison, endpointname, comparename, ComparisonOperation.EQUAL));

        ControlFlowGraphBuilder cfgBuilder = builder.getControlFlowGraphBuilder();
        LoopTreeBuilder loopTreeBuilder = builder.getLoopTreeBuilder();

        // build the iftrue part
        TBlock iftrue = cfgBuilder.newBlock();
        TBlock iffalse = cfgBuilder.newBlock();

        TIfStatement ifStatement = new TIfStatement(comparison, iftrue, iffalse);
        builder.add(ifStatement);

        loopTreeBuilder.ifStatement(ifStatement, iftrue, iffalse);

        // continue in a new block
        TBlock continuation = cfgBuilder.newBlock();

        cfgBuilder.addSuccessor(iftrue);
        cfgBuilder.addSuccessor(iffalse);
        cfgBuilder.pushBlock(iftrue);

        doVerifyOneMac(builder, op, dispatcher, data, endOfData);

        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.elseStatement(comparison);
        cfgBuilder.pushBlock(iffalse);
        // nothing to do otherwise
        cfgBuilder.addSuccessor(continuation);
        cfgBuilder.popBlock();

        loopTreeBuilder.endIfStatement(comparison);
        loopTreeBuilder.addBasicBlock(continuation);
        cfgBuilder.replaceBlock(continuation);
    }

    private void doVerifyOneMac(TypedIRBuilder builder, SecurityOperation op, int dispatcher, int data, int endOfData) {
        ArrayType byteArray = new ArrayType(PrimitiveType.BYTE);

        int key = builder.allocateRegister(IntrinsicTypes.KEY);
        int keyId = builder.allocateRegister(PrimitiveType.INT32);

        builder.add(new TImmediateLoad(PrimitiveType.INT32, keyId, op.getKey().getKeyId()));
        builder.add(IntrinsicFactory.createLoadKey(key, dispatcher, keyId));

        int packetLength = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TMove(PrimitiveType.INT32, packetLength, endOfData));

        int macSize = mechanism.getMACSize();
        int numMACs = op.getFlow().getNumMACs();
        int ourMAC = op.getOffset();

        // outMACOffset = packetLength - (numMACs - ourMAC) * macSize
        int ourMACOffset = builder.allocateRegister(PrimitiveType.INT32);
        int subtract = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, subtract, (numMACs - ourMAC) * macSize));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, ourMACOffset, packetLength, subtract, BinaryOperation.SUB));

        // endofdata = packetLength - numMACs * macSize
        int totalMACSize = builder.allocateRegister(PrimitiveType.INT32);
        builder.add(new TImmediateLoad(PrimitiveType.INT32, totalMACSize, numMACs * macSize));
        builder.add(new TBinaryArithOp(PrimitiveType.INT32, endOfData, packetLength, totalMACSize, BinaryOperation.SUB));

        builder.add(IntrinsicFactory.createVerifyMAC(data, endOfData, ourMACOffset, key));
    }

    public void run() throws FatalCompilerErrorException {
        if (!recordLevelEncryption) {
            driver.emitFatal(null, "field level encryption is not yet implemented");
        }

        for (Space space : app.getSpaces()) {
            for (ConcreteModel im : space.getModels()) {
                im.setEncryptCode(compileEncrypt(im));
                im.setDecryptCode(compileDecrypt(im));
            }
        }
    }
}
