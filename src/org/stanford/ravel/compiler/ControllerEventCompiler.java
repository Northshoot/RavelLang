package org.stanford.ravel.compiler;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.backend.CCodeTranslator;
import org.stanford.ravel.compiler.ir.AstToUntypedIRVisitor;
import org.stanford.ravel.compiler.ir.TypeResolvePass;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.error.FatalCompilerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ControllerEventCompiler {
    private final RavelCompiler driver;

    public ControllerEventCompiler(RavelCompiler driver) {
        this.driver = driver;
    }

    public TypedIR compileEvent(RavelParser.EventScopeContext tree) throws FatalCompilerErrorException {
        // hoist all variables up the register scope (the whole compilation unit)
        List<VariableSymbol> variables = new ArrayList<>();
        for (Symbol s : tree.scope.getAllSymbols()) {
            if (s instanceof VariableSymbol)
                variables.add((VariableSymbol)s);
        }
        // add controller level variables
        for (Symbol s : tree.scope.getEnclosingScope().getSymbols()) {
            if (s instanceof VariableSymbol)
                variables.add((VariableSymbol)s);
        }

        // compile to untyped IR
        AstToUntypedIRVisitor visitor = new AstToUntypedIRVisitor(this);
        visitor.visit(tree);

        System.out.println("event " + tree.scope.getName());
        for (VariableSymbol var : variables)
            System.out.println("var " + var.getName() + " @ " + var.getType().getName() + " : " + var.getRegister());
        System.out.println(visitor.getIR().getRoot());
        if (!driver.success())
            return null;

        TypeResolvePass typeResolvePass = new TypeResolvePass(this);
        for (VariableSymbol var : variables)
            typeResolvePass.declare(var);
        TypedIR ir2 = typeResolvePass.run(visitor.getIR());
        ControlFlowGraph cfg = ir2.getControlFlowGraph();

        System.out.println("CFG");
        cfg.visitForward(System.out::println);

        System.out.println("Loop Tree");
        System.out.println(ir2.getLoopTree());

        if (!driver.success())
            return null;

        ValidateIR.validate(ir2);

        // run analysis and optimization passes
        // lower IR

        return ir2;
    }

    public void emitError(SourceLocation loc, String message) {
        driver.emitError(loc, message);
    }
    public void emitWarning(SourceLocation loc, String message) {
        driver.emitWarning(loc, message);
    }
    public void emitFatal(SourceLocation loc, String message) throws FatalCompilerErrorException {
        driver.emitFatal(loc, message);
    }
}
