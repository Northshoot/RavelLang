package org.stanford.ravel.analysis;

import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.Space;

/**
 * A field tag is a tag applied to a variable meaning that either
 *
 * 1) the variable was set by reading a field of a record tagged with
 *    a model tag matching the given model and creator
 * 2) the variable was the sink of a instruction whose source was a variable
 *    of type 1)
 *
 * This is the global (meaning interprocedural/after we computed ownership
 * information) version of LocalOwnershipTaggingPass.FieldTag.
 *
 * Created by gcampagn on 2/10/17.
 */
public class FieldTag {
    public final Model model;
    public final Space creator;
    public final String field;

    public FieldTag(Model model, Space creator, String field) {
        this.model = model;
        this.creator = creator;
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTag fieldTag = (FieldTag) o;

        if (!model.equals(fieldTag.model)) return false;
        if (!creator.equals(fieldTag.creator)) return false;
        return field.equals(fieldTag.field);
    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + creator.hashCode();
        result = 31 * result + field.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return model.getName() + "." + field + " from " + creator.getName();
    }
}
