package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.compiler.symbol.InstanceSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.*;
import org.stanford.ravel.primitives.Fields.Field;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class JLang extends BaseLanguage {
    private static Logger LOGGER = Logger.getLogger(JLang.class.getName());
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/java/tmpl";
    private final static String RUNTIME_PKG = "org.stanford.ravel.rrt";

    private static final TypeFormatter JTYPES = new TypeFormatter() {
        @Override
        public String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "String";
                    case ERROR_MSG:
                        return RUNTIME_PKG + ".tiers.Error";
                    case DATE:
                    case DATE_TIME:
                    case TIMESTAMP:
                        return "java.util.Date";
                    case BYTE:
                        return "byte";
                    case INT32:
                        return "int";
                    case DOUBLE:
                        return "double";
                    case BOOL:
                        return "boolean";
                    case VOID:
                        return "void";

                    case ANY:
                    case ERROR:
                    default:
                        throw new AssertionError();
                }
            } else if (type instanceof ArrayType) {
                return toNativeType(((ArrayType) type).getElementType()) + "[]";
            } else if (type instanceof ClassType.InstanceType) {
                return ((ClassType.InstanceType) type).getClassType().getName();
            } else if (type instanceof ModelType.RecordType) {
                return ((ModelType.RecordType) type).getModel().getName() + ".Record";
            } else {
                return type.getName();
            }
        }
    };
    private static final LiteralFormatter JLITERAL = new CStyleLiteralFormatter();

    private final STGroup controllerGroup;
    private final STGroup modelGroup;
    private final STGroup irGroup;
    private final STGroup dispatcherGroup;

    public JLang() {
        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
        dispatcherGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/dispatcher.stg");
    }

    @Override
    public OptionParser getOptions() {
        return JavaLanguageOptions.getInstance();
    }

    private void setPackage(FileObject file, String packageName) {

    }

    private CodeModule simpleModule(ST tmpl, String name, String packageName) {
        tmpl.add("package", packageName);
        FileObject file = new FileObject();
        file.setFileName(name + ".java");
        file.setSubPath("src/" + packageName.replace(".", "/"));
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);
        return module;
    }

    @Override
    protected void createDispatcher(Space s) {
        // A helper class to pass down to StringTemplates
        class ConcreteController {
            public String name;
            public String varName;
            public final List<String> parameterValues = new ArrayList<>();
        }

        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST tmpl = dispatcherGroup.getInstanceOf("file");
        String packageName = options.getPackageName();

        for (InstantiatedModel im : s.getModels())
            tmpl.add("imports", packageName + ".models." + im.getName());
        for (InstantiatedController ictr : s.getControllers())
            tmpl.add("imports", packageName + ".controller." + ictr.getName());
        tmpl.add("imports", RUNTIME_PKG + ".tiers.Endpoint");
        tmpl.add("imports", RUNTIME_PKG + ".tiers.Error");
        tmpl.add("imports", RUNTIME_PKG + ".model.ModelBottomAPI");
        tmpl.add("imports", RUNTIME_PKG + ".RavelPacket");
        tmpl.add("imports", RUNTIME_PKG + ".DispatcherAPI");
        tmpl.add("imports", RUNTIME_PKG + ".SystemEventAPI");
        tmpl.add("imports", RUNTIME_PKG + ".DriverAPI");
        tmpl.add("imports", RUNTIME_PKG + ".SourceAPI");
        tmpl.add("imports", RUNTIME_PKG + ".SinkAPI");

        for (InstantiatedModel im : s.getModels())
            tmpl.add("models", im);
        for (InstantiatedController ictr : s.getControllers()) {
            ConcreteController concrete = new ConcreteController();
            concrete.name = ictr.getName();
            concrete.varName = ictr.getVarName();

            for (VariableSymbol sym : ictr.getController().getParameterSymbols()) {
                Object pvalue = ictr.getParam(sym.getName());

                if (pvalue instanceof InstantiatedModel) {
                    concrete.parameterValues.add("model_" + ((InstantiatedModel) pvalue).getVarName());
                } else if (pvalue instanceof Source) {
                    concrete.parameterValues.add("src_" + ((Source) pvalue).getName());
                } else if (pvalue instanceof Sink) {
                    concrete.parameterValues.add("sink_" + ((Sink) pvalue).getName());
                } else {
                    concrete.parameterValues.add(JLITERAL.toLiteral(pvalue));
                }
            }
            tmpl.add("controllers", concrete);
        }

        addModule(simpleModule(tmpl, "AppDispatcher", packageName));
    }

    @Override
    protected CodeModule createModel(InstantiatedModel im) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST modelTmpl = modelGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".models";
        modelTmpl.add("name", im.getName());

        String baseClass;
        switch (im.getBaseModel().getModelType()) {
            case LOCAL:
                baseClass = "LocalModel";
                break;
            case REPLICATED:
                baseClass = "ReplicatedModel";
                break;
            case STREAMING:
                baseClass = "StreamingModel";
                break;
            default:
                throw new AssertionError();
        }
        modelTmpl.add("imports", RUNTIME_PKG + ".model." + baseClass);
        modelTmpl.add("imports", RUNTIME_PKG + ".model.ModelRecord");
        modelTmpl.add("imports", RUNTIME_PKG + ".Context");
        modelTmpl.add("imports", options.getPackageName() + ".AppDispatcher");
        for (InstantiatedController ictr : im.getControllerList())
            modelTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getName());
        modelTmpl.add("base", baseClass);
        modelTmpl.add("modelObj", im);
        modelTmpl.add("modelFields", im.getBaseModel().getFields());
        modelTmpl.add("controllerList", im.getControllerList());
        modelTmpl.add("controllerMap", im.getControllerMap());

        return simpleModule(modelTmpl, im.getName(), packageName);
    }

    @Override
    protected CodeModule createController(InstantiatedController ictr) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();

        ST controllerTmpl = controllerGroup.getInstanceOf("file");
        String packageName = options.getPackageName() + ".controller";
        controllerTmpl.add("package", packageName);
        controllerTmpl.add("name", ictr.getName());
        controllerTmpl.add("imports", RUNTIME_PKG + ".Context");

        for (InstantiatedModel im : ictr.getLinkedModels()) {
            controllerTmpl.add("imports", options.getPackageName() + ".models." + im.getName());
        }

        STControllerTranslator.FileConfig fileConfig = new STControllerTranslator.FileConfig(ictr.getName() + ".java", controllerTmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Collections.singletonList(fileConfig), irGroup, JTYPES, JLITERAL);
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath("src/" + packageName.replace(".", "/"));
        return generated;
    }
}
