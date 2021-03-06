package ai.harmony.ravel.analysis;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.RavelCompiler;
import ai.harmony.ravel.compiler.types.ModelType;
import ai.harmony.ravel.primitives.*;

import java.util.Map;
import java.util.Set;

/**
 * For each event handler, compute which variables have values coming from
 * a model field, which fields the values come from, and who creates the corresponding
 * records.
 *
 * Created by gcampagn on 2/13/17.
 */
public class ModelOwnershipAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;

    public ModelOwnershipAnalysis(RavelCompiler driver, RavelApplication app, boolean debug) {
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

    // for debugging only
    private String getVarType(LinkedEvent event, int var) {
        return event.getHandler().getBody().getRegisterType(var).getName();
    }

    private void dumpAllOwnerships() {
        System.out.println("Ownership of event handler variables:");
        for (Space s : app.getSpaces()) {
            for (ConcreteController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    System.out.println(event);
                    event.getAllVariableCreators().forEach((var, creators) -> {
                        System.out.print(var + " @" + getVarType(event, var) + " [");
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
            for (ConcreteController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    System.out.println(event);
                    event.getAllVariableModelTags().forEach((var, tags) -> {
                        System.out.print(var + " @" + getVarType(event, var) + " [");
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
            for (ConcreteController ic : s.getControllers()) {
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
    private void tagOneVariableModelTagLocal(LinkedEvent handler, int variable) {
        handler.addVariableModelTag(variable, new ModelTag());
    }
    private void tagOneVariableFieldTag(LinkedEvent handler, int variable, Model model, Space space, String field) {
        assert model.getField(field) != null;
        handler.addVariableFieldTag(variable, new FieldTag(model, space, field));
    }
    private void tagOneVariableFieldTagLocal(LinkedEvent handler, int variable) {
        handler.addVariableFieldTag(variable, new FieldTag());
    }

    private void runLocalOwnership() {
        for (Space s : app.getSpaces()) {
            for (ConcreteController ic : s.getControllers()) {
                for (LinkedEvent event : ic) {
                    ModelEvent modelEvent;
                    Model eventModel;
                    EventHandler handler = event.getHandler();
                    if (handler.getEventType().getOwner() instanceof ModelType) {
                        eventModel = ((ConcreteModel) event.getComponent()).getBaseModel();
                        modelEvent = ModelEvent.valueOf(handler.getEventName());
                    } else {
                        eventModel = null;
                        modelEvent = null;
                    }

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
                                        tagOneVariableModelTag(event, var, m, s);
                                        break;

                                    case REMOTE:
                                        // variable is a record created by a different space
                                        assert m == eventModel;
                                        assert m.getReaders().contains(s);
                                        for (Space writer : m.getWriters()) {
                                            if (writer != s) {
                                                tagOneVariableCreator(event, var, writer);
                                                tagOneVariableModelTag(event, var, m, writer);
                                            }
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

                                        if (m.getModelType() == Model.Type.LOCAL) {
                                            tagOneVariableCreator(event, var, s);
                                            tagOneVariableModelTag(event, var, m, s);
                                        } else {
                                            for (Space writer : m.getWriters()) {
                                                tagOneVariableCreator(event, var, writer);
                                                tagOneVariableModelTag(event, var, m, writer);
                                            }
                                        }
                                        break;
                                }
                            }
                        } else {
                            // variable does not come from a model, it must be locally created
                            tagOneVariableCreator(event, var, s);
                            tagOneVariableModelTagLocal(event, var);
                        }

                        Set<LocalOwnershipTaggingPass.LocalFieldTag> fieldTags = allFieldTags.get(var);
                        if (fieldTags != null) {
                            for (LocalOwnershipTaggingPass.LocalFieldTag tag : fieldTags) {
                                Model m = app.getModel(tag.model.getName());
                                assert m != null;

                                switch (tag.creator) {
                                    case CREATED:
                                        // the value assigned to this field was created from this space
                                        tagOneVariableFieldTag(event, var, m, s, tag.field);
                                        break;
                                    case REMOTE:
                                        // the value assigned to this field was created in a different space
                                        assert eventModel != null;
                                        assert eventModel.getReaders().contains(s);
                                        for (Space writer : eventModel.getWriters()) {
                                            if (writer != s) {
                                                tagOneVariableFieldTag(event, var, m, writer, tag.field);
                                            }
                                        }
                                        break;
                                    case STORED:
                                        if (m.getModelType() == Model.Type.LOCAL) {
                                            tagOneVariableFieldTag(event, var, m, s, tag.field);
                                        } else {
                                            for (Space writer : m.getWriters()) {
                                                tagOneVariableFieldTag(event, var, m, writer, tag.field);
                                            }
                                        }
                                        break;
                                }
                            }
                        } else {
                            tagOneVariableFieldTagLocal(event, var);
                        }
                    }
                }
            }
        }
    }
}
