package org.stanford.ravel.api.lang;

import org.apache.commons.lang3.StringUtils;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.CodeModule;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.c.CCodeTranslator;
import org.stanford.ravel.api.lang.c.CLanguageOptions;
import org.stanford.ravel.compiler.ir.typed.TypedIR;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.*;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
    private final static String app_dir = "app_files/";

    private static final AttributeRenderer CTYPES = new AttributeRenderer() {
        private String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "const char *";
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
            } else if (type == SystemType.INSTANCE.getInstanceType() || type == SystemType.INSTANCE) {
                return "RavelSystemAPI*";
            } else if (type == IntrinsicTypes.KEY) {
                return "RavelKey*";
            } else if (type == IntrinsicTypes.ENDPOINT.getInstanceType()) {
                return "RavelEndpoint*";
            } else if (type instanceof ArrayType) {
                ArrayType arrayType = (ArrayType)type;
                if (arrayType.isKnownBound())
                    return toNativeType(arrayType.getElementType()) + "[" + arrayType.getBound() + "]";
                else
                    return toNativeType(arrayType.getElementType()) + "*";
            } else if (type instanceof ClassType.InstanceType) {
                return ((ClassType.InstanceType) type).getClassType().getName() + "*";
            } else if (type instanceof ModelType.RecordType) {
                return ((ModelType.RecordType) type).getModel().getName() + "_Record*";
            } else if (type instanceof ModelType.ContextType) {
                return "Context*";
            } else  {
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
            name = name.replace(".", "");
            return StringUtils.join(name.split("(?=\\p{Upper})"),"_").toLowerCase();
        }

        @Override
        public String toString(Object o, String s, Locale locale) {
            String name;

            if (o instanceof String)
                name = (String)o;
            else if (o instanceof Enum<?>)
                name = ((Enum<?>) o).name().toLowerCase();
            else
                name = o.toString();
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
        dispatcherGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/dispatcher.stg");
        dispatcherGroup.registerRenderer(Type.class, CTYPES);
        dispatcherGroup.registerRenderer(String.class, CIDENT);
        dispatcherGroup.registerRenderer(Model.Type.class, CIDENT);

        //irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
        //irGroup.registerRenderer(Type.class, CTYPES);
        //irGroup.registerRenderer(String.class, CIDENT);
        irTranslator = new CCodeTranslator(CTYPES, CIDENT, CLITERAL);

        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        controllerGroup.registerRenderer(Type.class, CTYPES);
        controllerGroup.registerRenderer(String.class, CIDENT);
        controllerGroup.registerRenderer(Model.Type.class, CIDENT);
        controllerGroup.registerRenderer(TypedIR.class, (Object o, String s, Locale locale) -> {
            TypedIR ir = (TypedIR)o;
            irTranslator.translate(ir);
            return irTranslator.getCode();
        });

        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        modelGroup.registerRenderer(Type.class, CTYPES);
        modelGroup.registerRenderer(String.class, CIDENT);
        modelGroup.registerRenderer(Model.Type.class, CIDENT);
        modelGroup.registerRenderer(TypedIR.class, (Object o, String s, Locale locale) -> {
            TypedIR ir = (TypedIR)o;
            irTranslator.translate(ir);
            return irTranslator.getCode();
        });
    }

    @Override
    public OptionParser getOptions() {
        return CLanguageOptions.getInstance();
    }

    @Override
    public CodeModule createInterface(ConcreteInterface iiface) {
        //TODO: fix dependency injection properly
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

        for (ConcreteControllerInstance ictr : iiface.getControllerList())
            iface_h.add("includes", app_dir + ictr.getComponent().getName() + ".h");
        iface_c.add("includes", app_dir + iiface.getName() + ".h");
        iface_c.add("includes", "AppDispatcher.h");
        iface_h.add("interface", iiface);
        iface_c.add("interface", iiface);

        CodeModule module = simpleModule(iface_h, iface_c, iiface.getName(), this.app_dir);

        //TODO: add
//        extra_includes
//        extra_src(path)
        ST extra_cflags = interfaceGroup.getInstanceOf("extra_cflags");
        if (extra_cflags != null) {
            module.buildSystemMeta.put("CFLAGS", extra_cflags.render());
        }
        ST extra_ldflags = interfaceGroup.getInstanceOf("extra_ldflags");
        if (extra_ldflags != null) {
            module.buildSystemMeta.put("LDFLAGS", extra_cflags.render());
        }
        ST extra_includes = interfaceGroup.getInstanceOf("extra_includes");
        if (extra_includes != null) {
            module.buildSystemMeta.put("EXTRA_INCLUDE", extra_cflags.render());
        }
        ST extra_src = interfaceGroup.getInstanceOf("extra_src");
        if (extra_src != null) {
            module.buildSystemMeta.put("EXTRA_SRC", extra_cflags.render());
        }
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
        ST dispatcher_h = dispatcherGroup.getInstanceOf("h_file");
        ST dispatcher_c = dispatcherGroup.getInstanceOf("c_file");

        for (ConcreteModel im : s.getModels())
            dispatcher_h.add("includes", this.app_dir + im.getName() + ".h");
        for (ConcreteInterface iiface : s.getInterfaces())
            dispatcher_h.add("includes", this.app_dir + iiface.getName() + ".h");
        for (org.stanford.ravel.primitives.ConcreteController ictr : s.getControllers())
            dispatcher_h.add("includes", this.app_dir + ictr.getName() + ".h");
        dispatcher_c.add("includes", "AppDispatcher.h");

        for (ConcreteModelInstance im : s.getModelInstances()) {
            dispatcher_h.add("models", im);
            dispatcher_c.add("models", im);
        }
        for (ConcreteInterfaceInstance iiface : s.getInterfaceInstances()) {
            dispatcher_h.add("interfaces", iiface);
            dispatcher_c.add("interfaces", iiface);
        }

        for (ConcreteControllerInstance ictr : s.getControllerInstances()) {
            ConcreteController concrete = new ConcreteController();
            concrete.name = ictr.getComponent().getName();
            concrete.varName = ictr.getVarName();

            for (VariableSymbol sym : ictr.getComponent().getController().getParameterSymbols()) {
                Object pvalue = ictr.getParam(sym.getName());

                if (pvalue instanceof ConcreteModelInstance) {
                    concrete.parameterValues.add("&self->model_" + ((ConcreteModelInstance) pvalue).getVarName());
                } else if (pvalue instanceof ConcreteInterfaceInstance) {
                    concrete.parameterValues.add("&self->iface_" + ((ConcreteInterfaceInstance) pvalue).getVarName());
                } else if (pvalue instanceof SystemAPIInstance) {
                    concrete.parameterValues.add("&self->base.sys_api");
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
    protected CodeModule createController(org.stanford.ravel.primitives.ConcreteController ictr) {
        ST h_tmpl = controllerGroup.getInstanceOf("h_file");
        h_tmpl.add("name", ictr.getName());
        for (ConcreteModel im : ictr.getLinkedModels()) {
            h_tmpl.add("includes", this.app_dir + im.getName() + ".h");
        }
        for (ConcreteInterface iiface : ictr.getLinkedInterfaces()) {
            h_tmpl.add("includes", this.app_dir + iiface.getName() + ".h");
        }

        STControllerTranslator.FileConfig h_file = new STControllerTranslator.FileConfig(ictr.getName() + ".h", h_tmpl);

        ST c_tmpl = controllerGroup.getInstanceOf("c_file");
        c_tmpl.add("name", ictr.getName());
        //TODO: removed "controller/" +
        c_tmpl.add("includes", this.app_dir + ictr.getName() + ".h");
        c_tmpl.add("includes", "AppDispatcher.h");

        STControllerTranslator.FileConfig c_file = new STControllerTranslator.FileConfig(ictr.getName() + ".c", c_tmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Arrays.asList(h_file, c_file), irTranslator);
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath(this.app_dir);
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
        //TODO: need fix for the build system
        cFile.setSubPath(this.app_dir);
        cFile.setContent(c_tmpl.render());

        CodeModule module = new CodeModule();
        module.addFile(hFile);
        module.addFile(cFile);
        return module;
    }

    @Override
    protected CodeModule createModel(ConcreteModel im) {
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

        for (ConcreteControllerInstance ictr : im.getControllerList())
            model_h.add("includes", this.app_dir + ictr.getComponent().getName() + ".h");
        model_c.add("includes", this.app_dir + im.getName() + ".h");
        model_c.add("includes", "AppDispatcher.h");
        model_h.add("base", baseClass);
        model_c.add("base", baseClass);
        model_h.add("model", im);
        model_c.add("model", im);

        return simpleModule(model_h, model_c, im.getName(), this.app_dir);
    }
}
