package edu.stanford.ravel;

import edu.stanford.ravel.compiler.SourceLocation;
import edu.stanford.ravel.compiler.scope.Scope;
import edu.stanford.ravel.compiler.symbol.*;
import edu.stanford.ravel.error.FatalCompilerErrorException;
import edu.stanford.ravel.primitives.Interface;

/**
 * Compile the interface from Symbol definitions to
 * primitive Interface objects
 *
 * Created by gcampagn on 1/30/17.
 */
class InterfaceCompiler {
    private final RavelApplication app;
    private final RavelCompiler driver;

    public InterfaceCompiler(RavelApplication app, RavelCompiler driver) {
        this.app = app;
        this.driver = driver;
    }

    public Interface compileInterface(InterfaceSymbol symbol) throws FatalCompilerErrorException {
        Interface iface = new Interface(symbol.getName(), symbol);

        for (Symbol s : symbol.getSymbols()) {
            if (s instanceof VariableSymbol)
                iface.addParameter((VariableSymbol)s);
        }

        Scope implementation = symbol.getImplementationScope();

        for (Symbol implSym : implementation.getSymbols()) {
            boolean good = false;
            if (implSym instanceof ConstantSymbol) {
                Object value = ((ConstantSymbol) implSym).getValue();
                if (value instanceof String) {
                    good = true;
                    iface.setImplementation(implSym.getName(), (String) value);
                }
            }
            if (!good) {
                driver.emitError(new SourceLocation(implSym.getDefNode()), "bad value for " + implSym.getName() + " implementation, must be a string");
            }
        }

        Scope config = symbol.getConfigurationScope();
        if (config != null) {
            for (Symbol s : config.getSymbols()) {
                if (s instanceof ConstantSymbol)
                    iface.setConstantProperty(s.getName(), ((ConstantSymbol) s).getValue());
                else if (s instanceof ReferenceSymbol) {
                    //TODO: argument values are not passed
//                    /System.out.println(config.getEnclosingScope().getAllSymbols());
                    iface.setReferenceProperty(s.getName(), ((ReferenceSymbol) s).getValue());
                }
                else
                    throw new AssertionError();
            }
        }

        Scope uses = symbol.getUsesScope();
        if (uses != null) {
            for (Symbol s : uses.getSymbols()) {
                ReferenceSymbol ref = (ReferenceSymbol)s;
                iface.dependOnModel(ref.getName(), app.getModel(ref.getValue()));
            }
        }

        for (Symbol defSym : symbol.getSymbols()) {
            if (defSym instanceof MethodDeclarationSymbol) {
                MethodDeclarationSymbol imsym = (MethodDeclarationSymbol) defSym;
                if (imsym.isEvent())
                    iface.addEvent(imsym.getName());
            }
        }

        return iface;
    }
}
