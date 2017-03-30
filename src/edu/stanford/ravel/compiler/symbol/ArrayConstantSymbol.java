package edu.stanford.ravel.compiler.symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gcampagn on 3/10/17.
 */
public class ArrayConstantSymbol extends VariableSymbol {
    private final List<Object> values = new ArrayList<>();

    public ArrayConstantSymbol(String name) {
        super(name);
    }

    public void addValue(Object value) {
        values.add(value);
    }

    public List<Object> getValues() {
        return Collections.unmodifiableList(values);
    }
}
