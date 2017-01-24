package org.stanford.ravel.compiler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.stanford.antlr4.RavelParser;
import org.stanford.api.platforms.SystemApi;
import org.stanford.ravel.compiler.ir.AstToUntypedIRVisitor;
import org.stanford.ravel.compiler.ir.TypeResolvePass;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.ir.untyped.FieldStore;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.EventSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.error.FatalCompilerErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ControllerCompiler {
    private boolean hadErrors = false;
    private List<CompileError> errors = new ArrayList<>();

    public ControllerCompiler() {}

    public boolean compileEvent(RavelParser.EventScopeContext tree) throws FatalCompilerErrorException {
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
        if (!success())
            return false;

        TypeResolvePass typeResolvePass = new TypeResolvePass(this);
        for (VariableSymbol var : variables)
            typeResolvePass.declare(var);
        TypedIR ir2 = typeResolvePass.run(visitor.getIR());
        ControlFlowGraph cfg = ir2.getControlFlowGraph();

        System.out.println("CFG");
        cfg.visitForward(System.out::println);

        if (!success())
            return false;

        ValidateIR.validate(ir2);

        // run analysis and optimization passes
        // lower IR

        return true;
    }

    public void printAllErrors() {
        for (CompileError error : errors) {
            System.out.println(error);
        }
    }

    public boolean success() {
        return !hadErrors;
    }

    public void emitError(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.ERROR, message));
        hadErrors = true;
    }
    public void emitWarning(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.WARNING, message));
    }
    public void emitFatal(SourceLocation loc, String message) throws FatalCompilerErrorException {
        errors.add(new CompileError(loc, CompileError.Severity.FATAL, message));
        hadErrors = true;
        throw new FatalCompilerErrorException();
    }
}
