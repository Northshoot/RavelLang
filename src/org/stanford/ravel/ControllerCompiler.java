package org.stanford.ravel;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ir.CompileToIRPass;
import org.stanford.ravel.compiler.ir.LowerIRPass;
import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.EventHandlerSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.EventHandler;

/**
 * Created by gcampagn on 1/30/17.
 */
class ControllerCompiler {
    private final RavelCompiler driver;
    private final boolean debug;

    public ControllerCompiler(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    public Controller preAnalysis(ControllerSymbol c) throws FatalCompilerErrorException {
        CompileToIRPass pass = new CompileToIRPass(driver, debug);
        Controller controller = new Controller(c.getName());

        controller.addAllParameters(c.getParameters());
        controller.addAllClassScopeVariables(c.getClassScopeVariables());
        int firstGpRegister = Registers.FIRST_GP_REG;
        for (Symbol s : c.getSymbols()) {
            if (s instanceof VariableSymbol) {
                VariableSymbol var = (VariableSymbol)s;
                int reg = var.getRegister();
                if (reg == Registers.UNSET_REG) {
                    reg = firstGpRegister++;
                    var.setRegister(reg);
                }
            }
        }

        for (EventHandlerSymbol eventSym : c.getEvents()) {
            VariableSymbol modelVar = (VariableSymbol) c.resolve(eventSym.getModelVarName());

            TypedIR ir = pass.run(eventSym, (RavelParser.EventScopeContext) eventSym.getDefNode(), firstGpRegister);
            if (ir != null) {
                EventHandler event = new EventHandler(modelVar, eventSym.getArguments(), eventSym.getType(), ir);
                controller.addEvent(event);
            }
        }

        return controller;
    }

    public void postAnalysis(Controller c) throws FatalCompilerErrorException {
        LowerIRPass pass = new LowerIRPass(driver, debug);

        for (EventHandler handler : c) {
            pass.run(handler.getBody());
        }
    }
}
