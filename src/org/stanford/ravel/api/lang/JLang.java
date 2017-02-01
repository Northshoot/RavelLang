package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.compiler.symbol.FieldSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.*;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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

    private static final AttributeRenderer JTYPES = new AttributeRenderer() {
        private String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "java.lang.String";
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
            } else if (type instanceof ModelType.ContextType) {
                return "Context<" + toNativeType(((ModelType.ContextType) type).getOwner()) + ".Record>";
            } else if (type instanceof SystemType) {
                return "DispatcherAPI";
            } else {
                return type.getName();
            }
        }

        @Override
        public String toString(Object o, String s, Locale locale) {
            return toNativeType((Type)o);
        }
    };
    private static final LiteralFormatter JLITERAL = new CStyleLiteralFormatter();

    private final STGroup controllerGroup;
    private final STGroup modelGroup;
    private final STGroup irGroup;
    private final STGroup dispatcherGroup;

    public JLang() {
        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        controllerGroup.registerRenderer(Type.class, JTYPES);
        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        modelGroup.registerRenderer(Type.class, JTYPES);
        dispatcherGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/dispatcher.stg");
        dispatcherGroup.registerRenderer(Type.class, JTYPES);
        irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
        irGroup.registerRenderer(Type.class, JTYPES);
    }

    @Override
    public OptionParser getOptions() {
        return JavaLanguageOptions.getInstance();
    }

    private CodeModule simpleModule(ST tmpl, String name, String packageName) {
        tmpl.add("name", name);
        tmpl.add("package", packageName);
        FileObject file = new FileObject();
        file.setFileName(name + ".java");
        file.setSubPath("src/" + packageName.replace(".", "/"));
        file.setContent(tmpl.render());
        CodeModule module = new CodeModule();
        module.addFile(file);
        return module;
    }

    // A helper class to pass down to StringTemplates
    private class ConcreteController {
        public String name;
        public String varName;
        public final List<String> parameterValues = new ArrayList<>();
    }

    @Override
    protected CodeModule createDispatcher(Space s) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST tmpl = dispatcherGroup.getInstanceOf("file");
        String packageName = options.getPackageName();

        for (InstantiatedModel im : s.getModels())
            tmpl.add("imports", packageName + ".models." + im.getName());
        for (InstantiatedInterface iiface : s.getInterfaces())
            tmpl.add("imports", packageName + ".interfaces." + iiface.getName());
        for (InstantiatedController ictr : s.getControllers())
            tmpl.add("imports", packageName + ".controller." + ictr.getName());

        for (InstantiatedModel im : s.getModels())
            tmpl.add("models", im);
        for (InstantiatedInterface iiface : s.getInterfaces())
            tmpl.add("interfaces", iiface);
        for (InstantiatedController ictr : s.getControllers()) {
            ConcreteController concrete = new ConcreteController();
            concrete.name = ictr.getName();
            concrete.varName = ictr.getVarName();

            for (VariableSymbol sym : ictr.getController().getParameterSymbols()) {
                Object pvalue = ictr.getParam(sym.getName());

                if (pvalue instanceof InstantiatedModel) {
                    concrete.parameterValues.add("model_" + ((InstantiatedModel) pvalue).getVarName());
                } else if (pvalue instanceof InstantiatedInterface) {
                    concrete.parameterValues.add("iface_" + ((InstantiatedInterface) pvalue).getVarName());
                } else if (pvalue instanceof SystemAPI) {
                    concrete.parameterValues.add("this");
                } else {
                    concrete.parameterValues.add(JLITERAL.toLiteral(pvalue));
                }
            }
            tmpl.add("controllers", concrete);
        }
        tmpl.add("space", s);

        return simpleModule(tmpl, "AppDispatcher", packageName);
    }

    @Override
    public CodeModule createInterface(InstantiatedInterface iiface) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();

        String ifaceGroupName = iiface.getBaseInterface().getImplementation("java");
        if (ifaceGroupName == null) {
            LOGGER.severe("Missing Java implementation of " + iiface.getName());
            return null;
        }

        STGroupFile ifaceGroup = new STGroupFile(ifaceGroupName);
        ifaceGroup.registerRenderer(Type.class, JTYPES);
        ST ifaceTmpl = ifaceGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".interfaces";
        ifaceTmpl.add("imports", options.getPackageName() + ".AppDispatcher");
        for (InstantiatedController ictr : iiface.getControllerList())
            ifaceTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getName());
        ifaceTmpl.add("interface", iiface);

        return simpleModule(ifaceTmpl, iiface.getName(), packageName);
    }

    @Override
    protected CodeModule createModel(InstantiatedModel im) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST modelTmpl = modelGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".models";

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
        modelTmpl.add("imports", options.getPackageName() + ".AppDispatcher");
        for (InstantiatedController ictr : im.getControllerList())
            modelTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getName());
        modelTmpl.add("base", baseClass);
        modelTmpl.add("model", im);
        modelTmpl.add("set_endpoints", im.getBaseModel().getModelType() == Model.Type.REPLICATED);

        return simpleModule(modelTmpl, im.getName(), packageName);
    }

    @Override
    protected CodeModule createController(InstantiatedController ictr) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();

        ST controllerTmpl = controllerGroup.getInstanceOf("file");
        String packageName = options.getPackageName() + ".controller";
        controllerTmpl.add("package", packageName);
        controllerTmpl.add("name", ictr.getName());

        for (InstantiatedModel im : ictr.getLinkedModels()) {
            controllerTmpl.add("imports", options.getPackageName() + ".models." + im.getName());
        }
        for (InstantiatedInterface iiface : ictr.getLinkedInterfaces()) {
            controllerTmpl.add("imports", options.getPackageName() + ".interfaces." + iiface.getName());
        }

        STControllerTranslator.FileConfig fileConfig = new STControllerTranslator.FileConfig(ictr.getName() + ".java", controllerTmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Collections.singletonList(fileConfig), new STIRTranslator(irGroup, JLITERAL));
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath("src/" + packageName.replace(".", "/"));
        return generated;
    }
}
