package org.stanford.ravel.compiler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ir.AstToUntypedIRVisitor;
import org.stanford.ravel.error.FatalCompilerErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class ControllerCompiler {
    private List<CompileError> errors = new ArrayList<>();

    public ControllerCompiler() {}

    public void compileEvent(RavelParser.EventScopeContext tree) {
        // compile to untyped IR
        AstToUntypedIRVisitor visitor = new AstToUntypedIRVisitor(this);
        visitor.visit(tree);

        System.out.println("event " + tree.qualified_name().getText());
        System.out.println(visitor.getIR().getRoot());

        // run type check, assign types to registers
        // compile to typed IR
        // run analysis and optimization passes
        // lower IR
    }

    public boolean success() {
        for (CompileError error : errors) {
            if (error.getSeverity() != CompileError.Severity.WARNING)
                return false;
        }
        return true;
    }

    public void emitError(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.ERROR, message));
    }
    public void emitWarning(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.WARNING, message));
    }
    public void emitFatal(SourceLocation loc, String message) throws FatalCompilerErrorException {
        errors.add(new CompileError(loc, CompileError.Severity.FATAL, message));
        throw new FatalCompilerErrorException();
    }
}
