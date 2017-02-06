package org.stanford.ravel.api.lang;

import org.apache.commons.lang3.StringUtils;
import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.c.CCodeTranslator;
import org.stanford.ravel.api.lang.c.CLanguageOptions;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.*;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class CLang extends BaseLanguage {
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/c/tmpl";

    private static final AttributeRenderer CTYPES = new AttributeRenderer() {
        private String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "char *";
                    case ERROR_MSG:
                        return "int";
                    case TIMESTAMP:
                        return "time_t";
                    case BYTE:
                        return "uint8_t";
                    case INT32:
                        return "int32_t";
                    case DOUBLE:
                        return "double";
                    case BOOL:
                        return "bool";
                    case VOID:
                        return "void";

                    case ANY:
                        return "void*";
                    case ERROR:
                    default:
                        throw new AssertionError();
                }
            } else if (type instanceof ArrayType) {
                return toNativeType(((ArrayType) type).getElementType()) + "*";
            } else if (type instanceof ClassType.InstanceType) {
                return ((ClassType.InstanceType) type).getClassType().getName() + "*";
            } else if (type instanceof ModelType.RecordType) {
                return ((ModelType.RecordType) type).getModel().getName() + "_Record*";
            } else if (type instanceof ModelType.ContextType) {
                return "Context*";
            } else {
                // everything else ought to be a pointer
                return type.getName() + "*";
            }
        }

        @Override
        public String toString(Object o, String s, Locale locale) {
            return toNativeType((Type)o);
        }
    };
    private static final AttributeRenderer CIDENT = new AttributeRenderer() {
        private String getUnderscoreName(String name) {
            if (name.endsWith("API"))
                name = name.substring(0, name.length()-3);
            return StringUtils.join(name.split("(?=\\p{Upper})"),"_").toLowerCase();
        }


        @Override
        public String toString(Object o, String s, Locale locale) {
            String name = (String)o;
            if (s == null)
                return name;

            switch (s) {
                case "function":
                    return getUnderscoreName(name);
                case "macro_def":
                    return getUnderscoreName(name).toUpperCase(locale);
                case "literal":
                    return CLITERAL.toLiteral(o);
                default:
                    return name;
            }
        }
    };
    private static final LiteralFormatter CLITERAL = new CStyleLiteralFormatter();

    private final STGroup controllerGroup;
    private final STGroup modelGroup;
    //private final STGroup irGroup;
    private final IRTranslator irTranslator;
    private final STGroup dispatcherGroup;

    public CLang() {
        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        controllerGroup.registerRenderer(Type.class, CTYPES);
        controllerGroup.registerRenderer(String.class, CIDENT);

        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        modelGroup.registerRenderer(Type.class, CTYPES);
        modelGroup.registerRenderer(String.class, CIDENT);

        dispatcherGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/dispatcher.stg");
        dispatcherGroup.registerRenderer(Type.class, CTYPES);
        dispatcherGroup.registerRenderer(String.class, CIDENT);

        //irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
        //irGroup.registerRenderer(Type.class, CTYPES);
        //irGroup.registerRenderer(String.class, CIDENT);
        irTranslator = new CCodeTranslator(CTYPES, CIDENT, CLITERAL);
    }

    @Override
    public OptionParser getOptions() {
        return CLanguageOptions.getInstance();
    }

    @Override
    public CodeModule createInterface(InstantiatedInterface iiface) {
        String ifaceGroupName = iiface.getBaseInterface().getImplementation("c");
        if (ifaceGroupName == null) {
            LOGGER.severe("Missing C implementation of " + iiface.getName());
            return null;
        }

        STGroup interfaceGroup = new STGroupFile(ifaceGroupName);
        interfaceGroup.registerRenderer(Type.class, CTYPES);
        interfaceGroup.registerRenderer(String.class, CIDENT);
        ST iface_h = interfaceGroup.getInstanceOf("h_file");
        ST iface_c = interfaceGroup.getInstanceOf("c_file");

        for (InstantiatedController ictr : iiface.getControllerList())
            iface_h.add("includes", "controller/" + ictr.getName() + ".h");
        iface_c.add("includes", "interfaces/" + iiface.getName() + ".h");
        iface_c.add("includes", "AppDispatcher.h");
        iface_h.add("interface", iiface);
        iface_c.add("interface", iiface);

        return simpleModule(iface_h, iface_c, iiface.getName(), "interfaces");
    }

    // A helper class to pass down to StringTemplates
    private class ConcreteController {
        public String name;
        public String varName;
        public final List<String> parameterValues = new ArrayList<>();
    }

    @Override
    protected CodeModule createDispatcher(Space s) {
        ST dispatcher_h = dispatcherGroup.getInstanceOf("h_file");
        ST dispatcher_c = dispatcherGroup.getInstanceOf("c_file");

        for (InstantiatedModel im : s.getModels())
            dispatcher_h.add("includes", "models/" + im.getName() + ".h");
        for (InstantiatedInterface iiface : s.getInterfaces())
            dispatcher_h.add("includes", "interfaces/" + iiface.getName() + ".h");
        for (InstantiatedController ictr : s.getControllers())
            dispatcher_h.add("includes", "controller/" + ictr.getName() + ".h");
        dispatcher_c.add("includes", "AppDispatcher.h");

        for (InstantiatedModel im : s.getModels()) {
            dispatcher_h.add("models", im);
            dispatcher_c.add("models", im);
        }
        for (InstantiatedInterface iiface : s.getInterfaces()) {
            dispatcher_h.add("interfaces", iiface);
            dispatcher_c.add("interfaces", iiface);
        }
        for (InstantiatedController ictr : s.getControllers()) {
            ConcreteController concrete = new ConcreteController();
            concrete.name = ictr.getName();
            concrete.varName = ictr.getVarName();

            for (VariableSymbol sym : ictr.getController().getParameterSymbols()) {
                Object pvalue = ictr.getParam(sym.getName());

                if (pvalue instanceof InstantiatedModel) {
                    concrete.parameterValues.add("&self->model_" + ((InstantiatedModel) pvalue).getVarName());
                } else if (pvalue instanceof InstantiatedInterface) {
                    concrete.parameterValues.add("&self->iface_" + ((InstantiatedInterface) pvalue).getVarName());
                } else if (pvalue instanceof SystemAPI) {
                    concrete.parameterValues.add("&self->sys_api");
                } else {
                    concrete.parameterValues.add(CLITERAL.toLiteral(pvalue));
                }
            }
            dispatcher_h.add("controllers", concrete);
            dispatcher_c.add("controllers", concrete);
        }
        dispatcher_h.add("space", s);
        dispatcher_c.add("space", s);

        return simpleModule(dispatcher_h, dispatcher_c, "AppDispatcher", "");
    }

    @Override
    protected CodeModule createController(InstantiatedController ictr) {
        ST h_tmpl = controllerGroup.getInstanceOf("h_file");
        h_tmpl.add("name", ictr.getName());
        for (InstantiatedModel im : ictr.getLinkedModels()) {
            h_tmpl.add("includes", "models/" + im.getName() + ".h");
        }
        for (InstantiatedInterface iiface : ictr.getLinkedInterfaces()) {
            h_tmpl.add("includes", "interfaces/" + iiface.getName() + ".h");
        }

        STControllerTranslator.FileConfig h_file = new STControllerTranslator.FileConfig(ictr.getName() + ".h", h_tmpl);

        ST c_tmpl = controllerGroup.getInstanceOf("c_file");
        c_tmpl.add("name", ictr.getName());
        c_tmpl.add("includes", "controller/" + ictr.getName() + ".h");
        c_tmpl.add("includes", "AppDispatcher.h");

        STControllerTranslator.FileConfig c_file = new STControllerTranslator.FileConfig(ictr.getName() + ".c", c_tmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Arrays.asList(h_file, c_file), irTranslator);
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath("controller");
        return generated;
    }

    private CodeModule simpleModule(ST h_tmpl, ST c_tmpl, String name, String subpath) {
        h_tmpl.add("name", name);
        FileObject hFile = new FileObject();
        hFile.setFileName(name + ".h");
        hFile.setSubPath(subpath);
        hFile.setContent(h_tmpl.render());

        c_tmpl.add("name", name);
        FileObject cFile = new FileObject();
        cFile.setFileName(name + ".c");
        cFile.setSubPath(subpath);
        cFile.setContent(c_tmpl.render());

        CodeModule module = new CodeModule();
        module.addFile(hFile);
        module.addFile(cFile);
        return module;
    }

    @Override
    protected CodeModule createModel(InstantiatedModel im) {
        ST model_h = modelGroup.getInstanceOf("h_file");
        ST model_c = modelGroup.getInstanceOf("c_file");

        String baseClass;
        switch (im.getBaseModel().getModelType()) {
            case LOCAL:
                baseClass = "RavelLocalModel";
                break;
            case REPLICATED:
                baseClass = "RavelReplicatedModel";
                break;
            case STREAMING:
                baseClass = "RavelStreamingModel";
                break;
            default:
                throw new AssertionError();
        }

        for (InstantiatedController ictr : im.getControllerList())
            model_h.add("includes", "controller/" + ictr.getName() + ".h");
        model_c.add("includes", "models/" + im.getName() + ".h");
        model_c.add("includes", "AppDispatcher.h");
        model_h.add("base", baseClass);
        model_c.add("base", baseClass);
        model_h.add("model", im);
        model_c.add("model", im);

        return simpleModule(model_h, model_c, im.getName(), "models");
    }
}
