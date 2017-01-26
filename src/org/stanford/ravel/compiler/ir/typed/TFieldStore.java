package org.stanford.ravel.compiler.ir.typed;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.Type;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TFieldStore extends TInstruction {
    public final Type type;
    public final CompoundType compoundType;
    public final int object;
    public final int value;
    public final String field;

    public TFieldStore(Type type, CompoundType compoundType, int object, String field, int value) {
        this.type = type;
        this.compoundType = compoundType;
        this.object = object;
        this.field = field;
        this.value = value;
    }

    public String toString() {
        return "field.st@" + type.getName() + " " + object + " ." + field + " = " + value;
    }

    @Override
    int[] getSources() {
        return new int[] { object, value };
    }

    @Override
    Type[] getSourceTypes() {
        return new Type[] { compoundType, type };
    }

    @Override
    public boolean writesMemory() {
        return true;
    }

    @Override
    public void accept(TInstructionVisitor visitor) {
        visitor.visit(this);
    }
}
