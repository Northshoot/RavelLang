package edu.stanford.ravel;

import edu.stanford.antlr4.RavelParser;
import edu.stanford.ravel.compiler.ir.CompileToIRPass;
import edu.stanford.ravel.compiler.ir.LowerIRPass;
import edu.stanford.ravel.compiler.ir.Registers;
import edu.stanford.ravel.compiler.ir.typed.TypedIR;
import edu.stanford.ravel.compiler.symbol.ControllerSymbol;
import edu.stanford.ravel.compiler.symbol.EventHandlerSymbol;
import edu.stanford.ravel.compiler.symbol.Symbol;
import edu.stanford.ravel.compiler.symbol.VariableSymbol;
import edu.stanford.ravel.compiler.types.ViewType;
import edu.stanford.ravel.error.FatalCompilerErrorException;
import edu.stanford.ravel.primitives.Controller;
import edu.stanford.ravel.primitives.EventHandler;

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
        controller.addAllArrayConstants(c.getArrayConstantVariables());
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
        for (Symbol s : c.getParameters()) {
            if (s instanceof VariableSymbol) {
                VariableSymbol var = (VariableSymbol)s;
                if (var.getType() instanceof ViewType) {
                    controller.addView(var);
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
