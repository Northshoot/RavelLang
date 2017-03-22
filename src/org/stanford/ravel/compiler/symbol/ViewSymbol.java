package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.types.ViewType;

/**
 * Created by gcampagn on 3/22/17.
 */
public class ViewSymbol extends ComponentSymbol {
    private final ViewType definedType;

    public ViewSymbol(String name) {
        super(name);

        definedType = new ViewType(this);
    }

    @Override
    public ViewType getDefinedType() {
        return definedType;
    }

    public void createInterfaceType() {
        for (Symbol sym : getSymbols()) {
            assert sym instanceof MethodDeclarationSymbol || sym instanceof VariableSymbol;
            if (sym instanceof MethodDeclarationSymbol) {
                MethodDeclarationSymbol imsym = (MethodDeclarationSymbol) sym;

                if (imsym.isEvent())
                    definedType.addEvent(imsym.getName(), imsym.getArguments());
                else
                    definedType.addMethod(imsym.getName(), imsym.getArguments(), imsym.getReturnValue());
            }
        }
    }

    public Scope getUsesScope() {
        return getNestedScope("uses");
    }
}
