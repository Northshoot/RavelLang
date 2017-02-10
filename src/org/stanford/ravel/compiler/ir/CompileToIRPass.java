package org.stanford.ravel.compiler.ir;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.typed.ControlFlowGraph;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.ir.typed.ValidateIR;
import org.stanford.ravel.compiler.ir.typed.ValidateSSA;
import org.stanford.ravel.compiler.symbol.EventHandlerSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.error.FatalCompilerErrorException;

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

        // hoist all variables up the register scope (the whole compilation unit)
        Set<VariableSymbol> variables = new HashSet<>();
        for (Symbol s : tree.scope.getAllSymbols()) {
            if (s instanceof VariableSymbol)
                variables.add((VariableSymbol)s);
        }
        // add controller level variables
        for (Symbol s : tree.scope.getEnclosingScope().getSymbols()) {
            if (s instanceof VariableSymbol)
                variables.add((VariableSymbol)s);
        }
        // add event arguments
        for (VariableSymbol s : declaredArguments) {
            variables.add(s);
        }

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
        for (VariableSymbol var : variables)
            typeResolvePass.declare(var);
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

        IntoSSAPass intoSSA = new IntoSSAPass(ir2);
        intoSSA.run();

        ValidateIR.validate(ir2);
        ValidateSSA.validate(ir2);
        if (debug) {
            System.out.println("SSA CFG");
            cfg.visitForward(System.out::println);
        }

        // run local (non interprocedural) analysis and optimization passes

        DeadValueEliminationPass deadValueEliminationPass = new DeadValueEliminationPass(ir2);
        boolean progress;
        int pass = 0;
        do {
            progress = false;
            progress = deadValueEliminationPass.run() || progress;
            if (debug && progress) {
                System.out.println("Opt pass #" + (pass+1));
                cfg.visitForward(System.out::println);
            }
            pass++;
        } while(progress);

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
