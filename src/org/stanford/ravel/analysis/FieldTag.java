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
 * A special kind of field tag (for which {@link FieldTag#isLocal()} is true) is
 * applied to event variables that are fully local.
 *
 * Created by gcampagn on 2/10/17.
 */
public class FieldTag {
    private final boolean isEmpty;
    public final Model model;
    public final Space creator;
    public final String field;

    public FieldTag() {
        isEmpty = true;
        model = null;
        creator = null;
        field = null;
    }

    public FieldTag(Model model, Space creator, String field) {
        isEmpty = false;
        this.model = model;
        this.creator = creator;
        this.field = field;
    }

    public boolean isLocal() {
        return isEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTag fieldTag = (FieldTag) o;

        if (isEmpty != fieldTag.isEmpty) return false;
        if (model != null ? !model.equals(fieldTag.model) : fieldTag.model != null) return false;
        if (creator != null ? !creator.equals(fieldTag.creator) : fieldTag.creator != null) return false;
        return field != null ? field.equals(fieldTag.field) : fieldTag.field == null;
    }

    @Override
    public int hashCode() {
        int result = (isEmpty ? 1 : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return isEmpty ? "local value" : (model.getName() + "." + field + " from " + creator.getName());
    }
}
