package org.stanford.ravel;

import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.*;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.Interface;

/**
 * Compile the interface from Symbol definitions to
 * primitive Interface objects
 *
 * Created by gcampagn on 1/30/17.
 */
class InterfaceCompiler {
    private final RavelCompiler driver;

    public InterfaceCompiler(RavelCompiler driver) {
        this.driver = driver;
    }

    public Interface compileInterface(InterfaceSymbol symbol) throws FatalCompilerErrorException {
        Interface iface = new Interface(symbol.getName(), symbol);

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
                    iface.addConstantProperty(s.getName(), ((ConstantSymbol) s).getValue());
                else if (s instanceof ReferenceSymbol)
                    iface.addReferenceProperty(s.getName(), ((ReferenceSymbol) s).getValue());
                else
                    throw new AssertionError();
            }
        }

        for (Symbol defSym : symbol.getSymbols()) {
            InterfaceMemberSymbol imsym = (InterfaceMemberSymbol)defSym;
            if (imsym.isEvent())
                iface.addEvent(imsym.getName());
        }

        return iface;
    }
}
