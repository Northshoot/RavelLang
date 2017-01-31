package org.stanford.ravel;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ControllerEventCompiler;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.EventHandlerSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.EventHandler;

import java.util.List;

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

    public Controller compile(ControllerSymbol c) throws FatalCompilerErrorException {
        ControllerEventCompiler compiler = new ControllerEventCompiler(driver, debug);

        Controller controller = new Controller(c.getName());

        controller.addAllParameters(c.getParameters());

        for (EventHandlerSymbol eventSym : c.getEvents()) {
            VariableSymbol modelVar = (VariableSymbol) c.resolve(eventSym.getModelVarName());

            TypedIR ir = compiler.compileEvent(eventSym, (RavelParser.EventScopeContext) eventSym.getDefNode());
            if (ir != null) {
                EventHandler event = new EventHandler(modelVar, eventSym.getArguments(), eventSym.getType(), ir);
                controller.addEvent(event);
            }
        }

        return controller;
    }
}
