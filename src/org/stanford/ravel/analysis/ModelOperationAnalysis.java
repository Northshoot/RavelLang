package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.primitives.*;

import java.util.Map;
import java.util.Set;

/**
 * Compute which instantiated controller does what on each field of each model,
 * based on the instructions on the event handlers
 *
 * Created by gcampagn on 2/9/17.
 */
public class ModelOperationAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private boolean progress;

    public ModelOperationAnalysis(RavelCompiler driver, RavelApplication app, boolean debug) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
    }

    public void run() {
        // tag each variable in each event handler in each instantiated controller
        // in each space with all the spaces from which the data can come from
        //
        // run the ownership tagging pass
        // note that we don't need to run this until convergence, because each variable belongs
        // to one and only event handler, and we run the local pass until convergence
        runLocalOwnership();

        if (debug) {
            dumpAllOwnerships();
            dumpAllModelTags();
            dumpAllFieldTags();
        }
    }

    private void dumpAllOwnerships() {
        System.out.println("Ownership of event handler variables:");
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    System.out.println(event);
                    event.getAllVariableCreators().forEach((var, creators) -> {
                        System.out.print(var + " [");
                        for (Space creator : creators) {
                            System.out.print(creator.getName() + ", ");
                        }
                        System.out.println("]");
                    });
                    System.out.println();
                }
            }
        }
    }

    private void dumpAllModelTags() {
        System.out.println("Model tags of event handler variables:");
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    System.out.println(event);
                    event.getAllVariableModelTags().forEach((var, tags) -> {
                        System.out.print(var + " [");
                        for (ModelTag tag : tags) {
                            System.out.print(tag + ", ");
                        }
                        System.out.println("]");
                    });
                    System.out.println();
                }
            }
        }
    }

    private void dumpAllFieldTags() {
        System.out.println("Field tags of event handler variables:");
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    System.out.println(event);
                    event.getAllVariableFieldTags().forEach((var, tags) -> {
                        System.out.print(var + " [");
                        for (FieldTag tag : tags) {
                            System.out.print(tag + ", ");
                        }
                        System.out.println("]");
                    });
                    System.out.println();
                }
            }
        }
    }

    private void tagOneVariableCreator(LinkedEvent handler, int variable, Space space) {
        handler.addVariableCreator(variable, space);
    }
    private void tagOneVariableModelTag(LinkedEvent handler, int variable, Model model, Space space) {
        handler.addVariableModelTag(variable, new ModelTag(model, space));
    }
    private void tagOneVariableFieldTag(LinkedEvent handler, int variable, Model model, Space space, String field) {
        handler.addVariableFieldTag(variable, new FieldTag(model, space, field));
    }

    private void runLocalOwnership() {
        for (Space s : app.getSpaces()) {
            for (InstantiatedController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    ModelEvent modelEvent;
                    EventHandler handler = event.getHandler();
                    if (handler.getEventType().getOwner() instanceof ModelType)
                        modelEvent = ModelEvent.valueOf(handler.getEventName());
                    else
                        modelEvent = null;

                    LocalOwnershipTaggingPass pass = new LocalOwnershipTaggingPass(handler.getBody(), modelEvent);
                    pass.run();

                    Map<Integer, Set<LocalOwnershipTaggingPass.LocalModelTag>> allModelTags = pass.getModelTags();
                    Map<Integer, Set<LocalOwnershipTaggingPass.LocalFieldTag>> allFieldTags = pass.getFieldTags();

                    for (int var : event.getHandler().getVariables()) {
                        if (allModelTags.containsKey(var)) {
                            Set<LocalOwnershipTaggingPass.LocalModelTag> modelTags = allModelTags.get(var);

                            for (LocalOwnershipTaggingPass.LocalModelTag tag : modelTags) {
                                Model m = app.getModel(tag.model.getName());
                                assert m != null;

                                switch (tag.creator) {
                                    case CREATED:
                                        // variable is a record created from this space
                                        tagOneVariableCreator(event, var, s);
                                        break;

                                    case REMOTE:
                                        // variable is a record created by a different space
                                        assert m.getReaders().contains(s);
                                        for (Space writer : m.getWriters()) {
                                            if (writer != s)
                                                tagOneVariableCreator(event, var, writer);
                                        }
                                        break;

                                    case STORED:
                                        // variable is a record created by any controller
                                        //
                                        // we could do a more precise analysis, and figure out that, eg
                                        // record id = 1 is created by controller 1, record id = 2 is created
                                        // by controller 2, etc.
                                        // (and in fact, this might be useful for
                                        m = app.getModel(tag.model.getName());
                                        assert m != null;

                                        for (Space writer : m.getWriters()) {
                                            tagOneVariableCreator(event, var, writer);
                                        }
                                }

                                for (Space creator : event.getVariableCreators(var)) {
                                    tagOneVariableModelTag(event, var, m, creator);
                                }
                            }
                        } else {
                            // variable does not come from a model, it must be locally created
                            tagOneVariableCreator(event, var, s);
                        }

                        Set<LocalOwnershipTaggingPass.LocalFieldTag> fieldTags = allFieldTags.get(var);
                        if (fieldTags != null) {
                            for (LocalOwnershipTaggingPass.LocalFieldTag tag : fieldTags) {
                                Model m = app.getModel(tag.model.getName());
                                assert m != null;

                                for (ModelTag modelTag : event.getVariableModelTags(var)) {
                                    tagOneVariableFieldTag(event, var, modelTag.model, modelTag.creator, tag.field);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
