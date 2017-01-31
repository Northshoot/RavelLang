package org.stanford.ravel.api.lang;

import org.apache.commons.lang3.StringUtils;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.c.CCodeTranslator;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.compiler.types.*;
import org.stanford.ravel.primitives.InstantiatedController;
import org.stanford.ravel.primitives.InstantiatedInterface;
import org.stanford.ravel.primitives.InstantiatedModel;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.logging.Logger;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class CLang extends BaseLanguage {
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/c/tmpl";

    private static final AttributeRenderer CTYPES = new AttributeRenderer() {
        private String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "char *";
                    case ERROR_MSG:
                        return "int";
                    case DATE:
                    case DATE_TIME:
                        return "struct tm";
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
    private final STGroup model_tmpl; // the old template file

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

        model_tmpl = new STGroupFile(BASE_LANG_TMPL_PATH +"/model_old.stg");
    }

    @Override
    public OptionParser getOptions() {
        return new LanguageOptions();
    }

    @Override
    protected CodeModule createDispatcher(Space space) {
        // TODO
        return null;
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

        STControllerTranslator.FileConfig c_file = new STControllerTranslator.FileConfig(ictr.getName() + ".c", c_tmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Arrays.asList(h_file, c_file), irTranslator);
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath("controller/");
        return generated;
    }

    @Override
    protected void createModels(Space space) {
        ST model_header = model_tmpl.getInstanceOf("models_header_file");
        model_header.add("space", space);
        FileObject f = new FileObject();
        f.setFileName("models.h");
        f.setContent(model_header.render());
        mFileObjects.add(f);
        ST model_object = model_tmpl.getInstanceOf("models_obj_file");
        model_object.add("space", space);
//        System.out.println(model_object.render());
        f = new FileObject();
        f.setFileName("models.c");
        f.setContent(model_object.render());
        mFileObjects.add(f);
    }
}
