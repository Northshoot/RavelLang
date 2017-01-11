package org.stanford.ravel.primitives;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.error.exceptions.TranslationError;
import org.stanford.ravel.translators.BaseXLingual;

/**
 * Created by lauril on 1/10/17.
 */
public class ComparisonOperator implements BaseXLingual {

    private RavelParser.Comp_opContext op;

    public ComparisonOperator(RavelParser.Comp_opContext tn){
        op = tn;

    }

    @Override
    public String getcName() throws TranslationError{
        //c and java have same comparators
        return getjName();
    }

    @Override
    public String getpName() throws TranslationError{
        if(op.GT() != null){
            return ">";
        } else if(op.LT() != null){
            return "<";
        } else if(op.EQUAL() != null){
            return "==";
        } else if(op.LE() != null){
            return "<=";
        } else if(op.GE() != null){
            return ">=";
        } else if(op.NOTEQUAL() != null){
            return "not";
        } else {
            throw new TranslationError("Could not find translation for " + op.getText());
        }
    }

    @Override
    public String getjName() throws TranslationError {
        if(op.GT() != null){
            return ">";
        } else if(op.LT() != null){
            return "<";
        } else if(op.EQUAL() != null){
            return "==";
        } else if(op.LE() != null){
            return "<=";
        } else if(op.GE() != null){
            return ">=";
        } else if(op.NOTEQUAL() != null){
            return "!=";
        } else {
            throw new TranslationError("Could not find translation for " + op.getText());
        }
    }
}
