package ai.harmony.ravel;

import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.compiler.ir.CompileToIRPass;
import ai.harmony.ravel.compiler.ir.LowerIRPass;
import ai.harmony.ravel.compiler.ir.Registers;
import ai.harmony.ravel.compiler.ir.typed.TypedIR;
import ai.harmony.ravel.compiler.symbol.ControllerSymbol;
import ai.harmony.ravel.compiler.symbol.EventHandlerSymbol;
import ai.harmony.ravel.compiler.symbol.Symbol;
import ai.harmony.ravel.compiler.symbol.VariableSymbol;
import ai.harmony.ravel.compiler.types.ViewType;
import ai.harmony.ravel.error.FatalCompilerErrorException;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.EventHandler;

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
