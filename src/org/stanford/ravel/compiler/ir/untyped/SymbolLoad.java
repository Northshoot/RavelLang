package org.stanford.ravel.compiler.ir.untyped;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.Symbol;

import java.util.Collections;
import java.util.List;

/**
 * Load a symbol (model, function, etc) into a pseudo-register
 *
 * This gets converted down into proper references at type check type
 *
 * Created by gcampagn on 1/20/17.
 */
public class SymbolLoad extends Instruction {
    public final int target;
    public final Symbol symbol;

    public SymbolLoad(ParserRuleContext definer, int target, Symbol symbol) {
        super(definer);

        this.target = target;
        this.symbol = symbol;
    }

    public String toString() {
        return "symbol " + target + " = " + symbol.getName();
    }

    @Override
    void accept(InstructionVisitor visitor) {
        visitor.visit(this);
    }
}
