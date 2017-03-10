package org.stanford.ravel.compiler.symbol;

/**
 * A symbol representing a reference to some external object (a source or a sink, defined
 * by the platform)
 *
 * Created by lauril on 8/26/16.
 */
public class ReferenceSymbol extends VariableSymbol {
    private final String mValue;

    public ReferenceSymbol(String name, String val) {
        super(name);
        this.mValue = val;
    }

    public String getValue(){
        return mValue;
    }
}
