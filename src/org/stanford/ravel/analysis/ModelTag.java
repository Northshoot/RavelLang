package org.stanford.ravel.analysis;

import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.Space;

/**
 * A model tag is a tag applied to a variable meaning that either
 *
 * 1) the variable is a record of the given model created by the given
 *    creator
 * 2) the variable is a context holding a record of the given model
 *    created by the given creator
 * 3) the variable was extracted from a field or an array of a variable of type 1)
 * 4) the variable is a record or array whose field or element was set
 *    by value of type 1), 2) or 3)
 * 4) the variable was computed from arithmetic operations from variables
 *    of any of the previous types
 *
 * This is the global (meaning interprocedural/after we computed ownership
 * information) version of LocalOwnershipTaggingPass.ModelTag.
 *
 * A special kind of field tag (for which {@link ModelTag#isLocal()} is true) is
 * applied to event variables that are fully local.
 *
 * Created by gcampagn on 2/10/17.
 */
public class ModelTag {
    private final boolean isEmpty;
    public final Model model;
    public final Space creator;

    public ModelTag() {
        isEmpty = true;
        model = null;
        creator = null;
    }

    public ModelTag(Model model, Space creator) {
        isEmpty = false;
        this.model = model;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelTag modelTag = (ModelTag) o;

        if (isEmpty != modelTag.isEmpty) return false;
        if (model != null ? !model.equals(modelTag.model) : modelTag.model != null) return false;
        return creator != null ? creator.equals(modelTag.creator) : modelTag.creator == null;
    }

    @Override
    public int hashCode() {
        int result = (isEmpty ? 1 : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return isEmpty ? "local value" : (model.getName() + " from " + creator.getName());
    }

    public boolean isLocal() {
        return isEmpty;
    }
}
