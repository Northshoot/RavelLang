package ai.harmony.ravel.compiler;

import ai.harmony.antlr4.RavelBaseListener;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.Scope;
import ai.harmony.ravel.compiler.symbol.ControllerSymbol;
import ai.harmony.ravel.compiler.symbol.ModelSymbol;
import ai.harmony.ravel.compiler.symbol.InstanceSymbol;

import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 8/19/16.
 */
public class RefPhase extends RavelBaseListener {

    GlobalScope globals;
    Scope currentScope; // resolve symbols starting in this scope


    public RefPhase(GlobalScope globals) {
        this.globals = globals;
        currentScope = globals;
    }


    /**
     * (1) Bellow we check if the models and controllers in the sapce are are declared before
     * (2) we add innit parameters to the models
     * @param ctx
     */
    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {
        currentScope = ctx.scope;
    }
    @Override
    public void enterModelInstanciation(RavelParser.ModelInstanciationContext ctx) {
        isDeclared( ModelSymbol.class, (List<InstanceSymbol>) ctx.scope.getAllSymbols() );

    }
    @Override
    public void enterControllerInstanciation(RavelParser.ControllerInstanciationContext ctx) {
        isDeclared( ControllerSymbol.class, (List<InstanceSymbol>) ctx.scope.getAllSymbols() );

    }

     private void isDeclared(Class<?> type, List<InstanceSymbol> mInstances){
         List<Scope> s = globals.getNestedScopesOfType(type);
         Map<String, Scope> name_map = Utils.listToNameMap(s);

         for(InstanceSymbol is: mInstances){
             String instance_name =
                     ((RavelParser.InstanceContext) is.getDefNode()).instance_name().getText();
             if(! name_map.containsKey(instance_name)) {
                 int line_err = is.getDefNode().start.getLine();
                 throw new RuntimeException("line: " + line_err + " Cant find symbol "
                         + instance_name + " for variable " + is.getName() );
             }
         }
     }
}
