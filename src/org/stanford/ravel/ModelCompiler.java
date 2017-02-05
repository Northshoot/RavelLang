package org.stanford.ravel;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.*;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.Model;

import java.util.*;

/**
 * Compile a ModelSymbol into the resulting primitive Model.
 *
 * Mostly it boils down to splitting the symbol declarations in the right
 * places, and never fails really.
 *
 * Created by lauril on 1/20/17.
 */
public class ModelCompiler {
    private final RavelCompiler driver;
    private final boolean debug;

    public ModelCompiler(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }

    public Model compile(ModelSymbol ms) throws FatalCompilerErrorException {
        // make concrete model
        String name = ms.getName();
        Model m = new Model(name, ms);

        for (Symbol s : ms.getSymbols()) {
            if (s instanceof VariableSymbol)
                m.addParameter(s.getName(), ((VariableSymbol) s).getType());
        }

        Scope propScope = ms.getNestedScope("properties");

        for (Symbol s : propScope.getAllSymbols()) {
            switch (s.getName()) {
                case "records":
                case "durable":
                case "reliable":
                    break;
                default:
                    driver.emitWarning(new SourceLocation(s.getDefNode()), "ignored model property " + s.getName());
            }

            if (s instanceof ConstantSymbol)
                m.addConstantProperty(s.getName(), ((ConstantSymbol) s).getValue());
            else if (s instanceof ReferenceSymbol)
                m.addReferenceProperty(s.getName(), ((ReferenceSymbol) s).getValue());
            else
                throw new AssertionError();
        }

        // create fields for the schema
        Scope schemaScope = ms.getNestedScope("schema");
        Collection<Symbol> schema = schemaScope.getAllSymbols();
        for (Symbol s : schemaScope.getSymbols()) {
            FieldSymbol fs = (FieldSymbol)s;
            m.addField(fs);
        }
        //end field constructions

        if (debug)
            System.out.println(m);
        return m;
    }
}
