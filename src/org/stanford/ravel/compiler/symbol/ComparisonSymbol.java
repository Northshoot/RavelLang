package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.primitives.ComparisonOperator;

/**
 * Created by lauril on 1/10/17.
 */
public class ComparisonSymbol extends BaseSymbol {


    protected int slot = -1;
    private Scope scope;
    private ComparisonOperator op;
    private String left;
    private String right;

    public ComparisonSymbol(String name, String left, String right, RavelParser.Comp_opContext comp_operators ){
        super(name);
        op = new ComparisonOperator(comp_operators);
        left = left;
        right = right;
    }

    public ComparisonOperator getOp(){
        return op;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public void setScope(Scope scope) {

    }

    @Override
    public int getInsertionOrderNumber() {
        return 0;
    }

    @Override
    public void setInsertionOrderNumber(int i) {

    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}
