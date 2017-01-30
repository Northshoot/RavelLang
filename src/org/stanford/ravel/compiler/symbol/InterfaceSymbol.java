package org.stanford.ravel.compiler.symbol;

import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.types.ControllerType;
import org.stanford.ravel.compiler.types.InterfaceType;
import org.stanford.ravel.compiler.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void createInterfaceType() {
        for (Symbol sym : getSymbols()) {
            assert sym instanceof InterfaceMemberSymbol;
            InterfaceMemberSymbol imsym = (InterfaceMemberSymbol)sym;

            if (imsym.isEvent())
                definedType.addEvent(imsym.getName(), imsym.getArguments(), imsym.getReturnValue());
            else
                definedType.addMethod(imsym.getName(), imsym.getArguments(), imsym.getReturnValue());
        }
    }
}
