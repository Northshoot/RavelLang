package ai.harmony.ravel.compiler.ir;

import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.RavelCompiler;
import ai.harmony.ravel.compiler.SourceLocation;
import ai.harmony.ravel.compiler.ir.typed.ControlFlowGraph;
import ai.harmony.ravel.compiler.ir.typed.TypedIR;
import ai.harmony.ravel.compiler.ir.typed.ValidateIR;
import ai.harmony.ravel.compiler.symbol.EventHandlerSymbol;
import ai.harmony.ravel.compiler.symbol.Symbol;
import ai.harmony.ravel.compiler.symbol.VariableSymbol;
import ai.harmony.ravel.compiler.types.PrimitiveType;
import ai.harmony.ravel.compiler.types.Type;
import ai.harmony.ravel.error.FatalCompilerErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compile each event from symbols to TypedIR in SSA form
 *
 * Created by gcampagn on 1/20/17.
 */
public class CompileToIRPass {
    private final boolean debug;
    private final RavelCompiler driver;
    private boolean hadError = false;

    public CompileToIRPass(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    public TypedIR run(EventHandlerSymbol eventSym, RavelParser.EventScopeContext tree, int firstGpRegister) throws FatalCompilerErrorException {
        // Check arguments
        List<VariableSymbol> declaredArguments = eventSym.getArguments();
        Type[] expected = eventSym.getType().getArgumentTypes();

        if (expected.length != declaredArguments.size()) {
            emitError(new SourceLocation(tree), "wrong number of arguments for event, expected " + expected.length + ", found " + declaredArguments.size());
        }
        for (int i = 0; i < Math.min(expected.length, declaredArguments.size()); i++) {
            Type exp = expected[i];
            Type decl = declaredArguments.get(i).getType();
            if (!exp.equals(decl)) {
                emitError(new SourceLocation(tree), "wrong type for argument " + (i+1) + ", must be " + exp.getName());
            }
        }

        // controllerScopeVariables is the set of names that can be accessed from the controller scope
        // temporaries is the set of names declared in the body of the event
        //
        // variables is the set of names that should be known to AstToUntypedIRVisitor and TypeResolvePass
        // (because they have not yet been assigned a register)
        Set<VariableSymbol> controllerScopeVariables = new HashSet<>();
        Set<VariableSymbol> variables = new HashSet<>();
        Set<VariableSymbol> temporaries = new HashSet<>();

        for (Symbol s : tree.scope.getEnclosingScope().getSymbols()) {
            if (s instanceof VariableSymbol)
                controllerScopeVariables.add((VariableSymbol) s);
        }
        // hoist all variables up the register scope (the whole compilation unit)
        for (Symbol s : tree.scope.getAllSymbols()) {
            if (s instanceof VariableSymbol && !declaredArguments.contains(s))
                temporaries.add((VariableSymbol)s);
        }


        // add controller level variables
        variables.addAll(controllerScopeVariables);
        // add event arguments
        variables.addAll(declaredArguments);
        // add temporaries
        variables.addAll(temporaries);

        // compile to untyped IR
        AstToUntypedIRVisitor visitor = new AstToUntypedIRVisitor(this, firstGpRegister);
        for (VariableSymbol var : declaredArguments)
            visitor.declare(var);
        visitor.visit(tree);

        if (debug) {
            System.out.println("event " + tree.scope.toQualifierString("."));
            for (VariableSymbol var : variables)
                System.out.println("var " + var.getName() + " @ " + var.getType().getName() + " : " + var.getRegister());
            System.out.println(visitor.getIR().getRoot());
        }
        if (hadError)
            return null;

        TypeResolvePass typeResolvePass = new TypeResolvePass(this);
        for (VariableSymbol var : controllerScopeVariables)
            typeResolvePass.declareParameter(var, true);
        for (VariableSymbol var : declaredArguments)
            typeResolvePass.declareParameter(var, false);
        for (VariableSymbol var : temporaries)
            typeResolvePass.declareTemporary(var);
        typeResolvePass.setReturnType(PrimitiveType.VOID);
        TypedIR ir2 = typeResolvePass.run(visitor.getIR());

        ControlFlowGraph cfg = ir2.getControlFlowGraph();

        if (debug) {
            System.out.println("CFG");
            cfg.visitForward(System.out::println);

            System.out.println("Loop Tree");
            System.out.println(ir2.getLoopTree());
        }

        if (hadError)
            return null;

        ValidateIR.validate(ir2);

        OptimizePass optimizer = new OptimizePass(ir2, debug);
        optimizer.run();

        return ir2;
    }

    public void emitError(SourceLocation loc, String message) {
        driver.emitError(loc, message);
        hadError = true;
    }
    public void emitWarning(SourceLocation loc, String message) {
        driver.emitWarning(loc, message);
    }
    public void emitFatal(SourceLocation loc, String message) throws FatalCompilerErrorException {
        driver.emitFatal(loc, message);
    }
}
