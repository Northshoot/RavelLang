package ai.harmony.ravel.compiler.symbol;

import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.types.InterfaceType;

/**
 * Created by gcampagn on 1/30/17.
 */
public class InterfaceSymbol extends ComponentSymbol {
    private final InterfaceType definedType;

    public InterfaceSymbol(String name) {
        super(name);

        definedType = new InterfaceType(this);
    }

    @Override
    public InterfaceType getDefinedType() {
        return definedType;
    }
    
    public Scope getImplementationScope() {
        return getNestedScope("implementation");
    }

    public Scope getConfigurationScope() {
        return getNestedScope("configuration");
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
