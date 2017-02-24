package org.stanford.ravel.analysis;

import org.stanford.ravel.compiler.ir.Registers;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.FunctionType;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.ModelEvent;

import java.util.*;

/**
 * "Tags" each variable/register in the IR with a set of model fields
 * that influence its value, and the creator (self or arrived) of that
 * model
 *
 * Created by gcampagn on 2/9/17.
 */
public class LocalOwnershipTaggingPass {
    /**
     * The creator of a record
     */
    public enum Creator {
        /**
         * The record came from a create() call
         */
        CREATED,

        /**
         * The record came from a self.record read inside a model.arrived event
         * (ie, it was definitely written by someone else)
         */
        REMOTE,

        /**
         * The record was stored in the model. This means it came from querying,
         * model.all(), or from the context of a departed event.
         */
        STORED
    }

    /**
     * A model tag is a tag applied to a variable meaning that either
     *
     * 1) the variable is a record of the given model created by the given
     *    creator
     * 2) the variable is a context holding a record of the given model
     *    created by the given creator
     * 3) the variable was extracted from a field of a variable of type 1)
     * 4) the variable was computed from arithmetic operations
     */
    public static class LocalModelTag {
        public final ModelType model;
        public final Creator creator;

        public LocalModelTag(ModelType model, Creator creator) {
            this.model = model;
            this.creator = creator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LocalModelTag modelTag = (LocalModelTag) o;

            if (!model.equals(modelTag.model)) return false;
            return creator == modelTag.creator;
        }

        @Override
        public int hashCode() {
            int result = model.hashCode();
            result = 31 * result + creator.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "LocalModelTag: " + model.getName() + " (" + creator.toString().toLowerCase() + ")";
        }
    }

    public static class LocalFieldTag {
        public final ModelType model;
        public final String field;

        public LocalFieldTag(ModelType model, String field) {
            this.model = model;
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LocalFieldTag fieldTag = (LocalFieldTag) o;

            if (!model.equals(fieldTag.model)) return false;
            return field.equals(fieldTag.field);
        }

        @Override
        public int hashCode() {
            int result = model.hashCode();
            result = 31 * result + field.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "LocalFieldTag: " + model.getName() + "." + field ;
        }
    }

    private Map<Integer, Set<LocalModelTag>> modelTags = new HashMap<>();
    private Map<Integer, Set<LocalFieldTag>> fieldTags = new HashMap<>();
    private final TypedIR ir;
    private final ModelEvent event;
    private boolean progress;

    public LocalOwnershipTaggingPass(TypedIR ir, ModelEvent event) {
        this.ir = ir;
        this.event = event;
    }

    public Map<Integer, Set<LocalModelTag>> getModelTags() {
        return modelTags;
    }
    public Map<Integer, Set<LocalFieldTag>> getFieldTags() {
        return fieldTags;
    }

    public void run() {
        // if we have a model, we have a context
        // depending on the event, that will result in different creators for
        // self.record
        if (event != null) {
            Type selfType = ir.getRegisterType(Registers.SELF_REG);
            assert selfType instanceof ModelType.ContextType;

            switch (event) {
                case arrived:
                    tagModel(Registers.SELF_REG, ((ModelType.ContextType) selfType).getOwner(), Creator.REMOTE);
                    break;
                case departed:
                case save_done:
                case full:
                    tagModel(Registers.SELF_REG, ((ModelType.ContextType) selfType).getOwner(), Creator.STORED);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        do {
            progress = false;
            ir.getControlFlowGraph().visitForward(this::visitBlock);
        } while(progress);
    }

    private void tagModel(int var, ModelType model, Creator creator) {
        LocalModelTag tag = new LocalModelTag(model, creator);

        Set<LocalModelTag> existing = modelTags.get(var);
        if (existing == null) {
            existing = new HashSet<>();
            existing.add(tag);
            modelTags.put(var, existing);
            progress = true;
        } else {
            progress = existing.add(tag) || progress;
        }
    }

    private void tagAllModels(int var, Set<LocalModelTag> tags) {
        if (tags == null)
            return;

        Set<LocalModelTag> existing = modelTags.get(var);
        if (existing == null) {
            existing = new HashSet<>();
            existing.addAll(tags);
            modelTags.put(var, existing);
            progress = true;
        } else {
            progress = existing.addAll(tags) || progress;
        }
    }

    private void tagField(int var, ModelType model, String field) {
        LocalFieldTag tag = new LocalFieldTag(model, field);

        Set<LocalFieldTag> existing = fieldTags.get(var);
        if (existing == null) {
            existing = new HashSet<>();
            existing.add(tag);
            fieldTags.put(var, existing);
            progress = true;
        } else {
            progress = existing.add(tag) || progress;
        }
    }

    private void tagAllFields(int var, Set<LocalFieldTag> tags) {
        if (tags == null)
            return;

        Set<LocalFieldTag> existing = fieldTags.get(var);
        if (existing == null) {
            existing = new HashSet<>();
            existing.addAll(tags);
            fieldTags.put(var, existing);
            progress = true;
        } else {
            progress = existing.addAll(tags) || progress;
        }
    }


    private void visitBlock(TBlock block) {
        for (TInstruction instr : block) {
            if (instr instanceof TMethodCall) {
                FunctionType type = ((TMethodCall) instr).type;
                if (type.getOwner() instanceof ModelType) {
                    // first the easy case: method calls on a model object
                    // we know the semantics of these, we can just encode them directly

                    switch (type.getFunctionName()) {
                        case "create":
                            tagModel(instr.getSink(), (ModelType) type.getOwner(), Creator.CREATED);
                            break;

                        case "all":
                        case "first":
                        case "last":
                        case "get":
                            tagModel(instr.getSink(), (ModelType) type.getOwner(), Creator.STORED);
                            break;

                        case "save":
                            // for each source (which is to say, the argument to .save()), propagate
                            // the model tags
                            //
                            // this means that if the record came from self.record, where self was remote,
                            // the new context will also be "remote"
                            // if the record came from .create(), the new context will be "created"
                            // if the record came from .get()/.all(), the new context will be "stored"
                            for (int source : instr.getSources()) {
                                tagAllModels(instr.getSink(), modelTags.get(source));
                                tagAllFields(instr.getSink(), fieldTags.get(source));
                            }
                            break;

                        case "clear":
                            assert instr.getSink() == Registers.VOID_REG;
                            break;

                        case "size":
                            break;

                        default:
                            throw new AssertionError("Unexpected model function " + type.getFunctionName());
                    }
                } else {
                    // NOTE! CAREFUL!
                    //
                    // two options:
                    //
                    // 1) the function is an interface function call
                    // 2) the function is a controller function call (NOT YET IMPLEMENTED)
                    //
                    // When 2) is implemented, we should just inline all function calls (or something else). We'll
                    // burn that bridge when we get to it. So we only deal with 1) for now.
                    //
                    // For 1, again two options:
                    // 1) the interface had a pointer to the model (NOT YET IMPLEMENTED)
                    // 2) the interface did not have a pointer to the model
                    //
                    // In the 1) case, the interface could magically come up with new records out of thin air,
                    // calling the actual low-level model functions, or do all sorts of crazy shit.
                    // In the 2) case, the interface can only compute something that depends on the input values
                    // and local information. If the input values are not remote model values, the output values
                    // will not be remote model values.
                    // 1) is not implemented yet, so we only care about 2) for now.
                    if (instr.getSink() != Registers.VOID_REG) {
                        for (int source : instr.getSources()) {
                            tagAllModels(instr.getSink(), modelTags.get(source));
                            tagAllFields(instr.getSink(), fieldTags.get(source));
                        }
                    }
                }
            } else if (instr instanceof TFieldLoad) {
                // a field load propagates the field and model tags of the source (the object from which the field
                // is read)
                for (int source : instr.getSources()) {
                    tagAllModels(instr.getSink(), modelTags.get(source));
                    tagAllFields(instr.getSink(), fieldTags.get(source));
                }

                // Additionally, when reading the field of a record, it creates a brand new field tag for that
                // concrete field
                //
                // This means that, if a record is read from one model and passed to another, it will be tagged
                // as the field of both
                //
                // FIXME: I'm not quite sure that's correct
                CompoundType compound = ((TFieldLoad) instr).compoundType;
                if (compound instanceof ModelType.RecordType) {
                    for (LocalModelTag modelTag : modelTags.get(((TFieldLoad) instr).source)) {
                        tagField(instr.getSink(), modelTag.model, ((TFieldLoad) instr).field);
                    }
                }
            } else if (instr instanceof TFieldStore) {
                // a field store "taints" the whole object where you store into
                // for the purposes of model tag analysis
                // (that is, if you write into a record field r1.f1 a value of another record r2,
                // effectively you're writing the whole record r2 into r1
                // which is mostly correct because when you later read r1.f1 you need the key
                // from r2
                // (although it's imprecise, as usual with pointer analysis)
                //
                // furthemore, if you write into a record, you create a brand new field tag for that concrete
                // field, and you put that in the written to variable
                // this is to say that the written variable carries a value coming from another source
                // in that field
                // we do this for this variable, and for all its aliases
                //
                // The tricky case that proves the need for alias analysis is:
                // r1 = m.create()
                // m.save(r1)
                // r2 = m.get(...)
                // r1.foo = v
                //
                // we need to say that r2 has a field tag of <foo, whatever model tag of v>
                // (this way, if later code does, say, system.print(r2.foo), we know that we need to decrypt
                // r2.foo based on how v came to be)

                CompoundType compound = ((TFieldStore) instr).compoundType;
                if (compound instanceof ModelType.RecordType) {
                    for (LocalModelTag modelTag : modelTags.getOrDefault(((TFieldStore) instr).value,Collections.emptySet())) {
                        tagField(instr.getSink(), modelTag.model, ((TFieldStore) instr).field);
                        for (int alias : ir.getAliases(instr.getSink())) {
                            tagField(alias, modelTag.model, ((TFieldStore) instr).field);
                        }
                    }
                }

                tagAllModels(((TFieldStore) instr).object, modelTags.get(((TFieldStore) instr).value));
                tagAllFields(((TFieldStore) instr).object, fieldTags.get(((TFieldStore) instr).value));
            } else if (instr instanceof TArrayStore) {
                // an array store is like a field store, except the field is an index
                // but we don't care about the index, we care about the object and the value
                //
                // in the value, value range propagation (or lowering constant arrays to variables)
                // could allow us to split constant arrays and constant array indices
                //
                // but arrays are rare (mostly used for "for r in model.all()" and the like, for which
                // the model tags are uniform anyway), constant arrays are not supported, and value
                // range propagation is a mess, so forget about it for now

                tagAllModels(((TArrayStore) instr).object, modelTags.get(((TArrayStore) instr).value));
                tagAllFields(((TArrayStore) instr).object, fieldTags.get(((TArrayStore) instr).value));
            } else if (instr instanceof TArrayLoad) {
                // an array load is the dual of an array store, and behaves like a field load, except
                // that we don't create new field tags, because w'ere never reading from a record

                tagAllModels(instr.getSink(), modelTags.get(((TArrayLoad) instr).source));
                tagAllFields(instr.getSink(), fieldTags.get(((TArrayLoad) instr).source));
            } else if (instr.getSink() != Registers.VOID_REG) {
                // all other instructions are phis, moves, converts, arithmethic operations, or control flow instructions
                // and such they just propagate the tags
                //
                // (it will be a later pass that will determine if decryption/encryption is necessary for these
                // operations to occur or we can just move the bits around)
                assert !instr.writesMemory();
                assert !instr.readsMemory();

                if (instr.getSink() != Registers.VOID_REG) {
                    for (int source : instr.getSources()) {
                        tagAllModels(instr.getSink(), modelTags.get(source));
                        tagAllFields(instr.getSink(), fieldTags.get(source));
                    }
                } else {
                    assert instr.affectsControlFlow();
                }
            }
        }
    }
}
