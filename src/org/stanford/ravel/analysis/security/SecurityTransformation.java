package org.stanford.ravel.analysis.security;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.OptimizePass;
import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.TIntrinsic;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.ir.typed.TypedIRBuilder;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.InstantiatedModel;
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

    public SecurityTransformation(RavelCompiler driver, RavelApplication app, boolean debug, boolean disableEncryption, boolean disableMAC) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
        this.enableEncryption = !disableEncryption;
        this.enableMAC = !disableMAC;
    }

    private TypedIR compileEncrypt(InstantiatedModel model, List<SecurityOperation> ops) {
        TypedIRBuilder builder = new TypedIRBuilder();
        // we never access any named registers in this context
        builder.setNextRegister(Registers.FIRST_GP_REG);

        ModelType.RecordType recordType = model.getBaseModel().getType().getRecordType();
        int recordReg = builder.allocateRegister(recordType);

        VariableSymbol sym = new VariableSymbol("record");
        sym.setType(recordType);
        sym.setRegister(recordReg);
        builder.declareParameter(sym, false);
        builder.setRegisterType(Registers.RETURN_REG, new ArrayType(PrimitiveType.BYTE));

        if (enableEncryption) {
            // TODO do something and encrypt
        } else {
            builder.addInstruction(new TIntrinsic(new ArrayType(PrimitiveType.BYTE), new Type[]{recordType}, Registers.RETURN_REG, "recordToBytes", new int[]{recordReg}));
        }

        if (enableMAC) {
            // TODO do something and mac
        } else {
            // nothing to do
        }

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Encrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();

        return ir;
    }

    private TypedIR compileDecrypt(InstantiatedModel model, List<SecurityOperation> ops) {
        TypedIRBuilder builder = new TypedIRBuilder();
        // we never access any named registers in this context
        builder.setNextRegister(Registers.FIRST_GP_REG);

        ModelType.RecordType recordType = model.getBaseModel().getType().getRecordType();
        int recordReg = builder.allocateRegister(new ArrayType(PrimitiveType.BYTE));
        VariableSymbol sym = new VariableSymbol("record");
        sym.setType(new ArrayType(PrimitiveType.BYTE));
        sym.setRegister(recordReg);
        builder.declareParameter(sym, false);
        builder.setRegisterType(Registers.RETURN_REG, recordType);

        if (enableEncryption) {
            // TODO do something and encrypt
        } else {
            builder.addInstruction(new TIntrinsic(recordType, new Type[]{new ArrayType(PrimitiveType.BYTE)}, Registers.RETURN_REG, "recordFromBytes", new int[]{recordReg}));
        }

        if (enableMAC) {
            // TODO do something and mac
        } else {
            // nothing to do
        }

        TypedIR ir = builder.finish();
        if (debug) {
            System.out.println("Decrypt IR for " + model);
            System.out.println(ir.getLoopTree());
        }
        OptimizePass opt = new OptimizePass(ir, false);
        opt.run();

        return ir;
    }

    public void run() {
        for (Space space : app.getSpaces()) {
            for (InstantiatedModel im : space.getModels()) {
                im.setReceiveCode(compileDecrypt(im, im.getSecurityOperations(true)));
                im.setSendCode(compileEncrypt(im, im.getSecurityOperations(false)));
            }
        }
    }
}
