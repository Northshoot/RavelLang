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
 * Created by gcampagn on 2/10/17.
 */
public class ModelTag {
    public final Model model;
    public final Space creator;

    public ModelTag(Model model, Space creator) {
        this.model = model;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelTag modelTag = (ModelTag) o;

        if (!model.equals(modelTag.model)) return false;
        return creator.equals(modelTag.creator);
    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + creator.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return model.getName() + " from " + creator.getName();
    }
}
