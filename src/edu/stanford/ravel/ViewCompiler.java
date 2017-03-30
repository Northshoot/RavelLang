package edu.stanford.ravel;

import edu.stanford.ravel.compiler.symbol.*;
import edu.stanford.ravel.compiler.scope.Scope;
import edu.stanford.ravel.primitives.View;
import edu.stanford.ravel.error.FatalCompilerErrorException;

/**
 * Created by gcampagn on 3/22/17.
 */
public class ViewCompiler {
    private final RavelApplication app;
    private final RavelCompiler driver;

    public ViewCompiler(RavelApplication app, RavelCompiler driver) {
        this.app = app;
        this.driver = driver;
    }

    public View compileView(ViewSymbol symbol) throws FatalCompilerErrorException {
        View view = new View(symbol.getName(), symbol);

        for (Symbol s : symbol.getSymbols()) {
            if (s instanceof VariableSymbol)
                view.addParameter((VariableSymbol)s);
        }

        Scope uses = symbol.getUsesScope();
        if (uses != null) {
            for (Symbol s : uses.getSymbols()) {
                ReferenceSymbol ref = (ReferenceSymbol)s;
                view.dependOnModel(ref.getName(), app.getModel(ref.getValue()));
            }
        }

        for (Symbol defSym : symbol.getSymbols()) {
            if (defSym instanceof MethodDeclarationSymbol) {
                MethodDeclarationSymbol imsym = (MethodDeclarationSymbol) defSym;
                if (imsym.isEvent())
                    view.addEvent(imsym);
                else
                    view.addFunction(imsym);
            }
        }

        return view;
    }
}
