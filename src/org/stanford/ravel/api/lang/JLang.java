package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.java.JavaLanguageOptions;
import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.ClassType;
import org.stanford.ravel.compiler.types.PrimitiveType;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Fields.Field;
import org.stanford.ravel.primitives.InstantiatedController;
import org.stanford.ravel.primitives.InstantiatedModel;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collections;
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
                        return "Exception";
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
            } else {
                return type.getName();
            }
        }
    };
    private static final LiteralFormatter JLITERAL = new CStyleLiteralFormatter();

    private final STGroup controllerGroup;
    private final STGroup modelGroup;
    private final STGroup irGroup;

    public JLang() {
        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
    }

    @Override
    public OptionParser getOptions() {
        return JavaLanguageOptions.getInstance();
    }

    @Override
    protected void createDispatcher(Space s) {
        // TODO
    }

    @Override
    protected CodeModule createModel(InstantiatedModel im) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST modelTmpl = modelGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".models";
        modelTmpl.add("package", packageName);
        modelTmpl.add("name", im.getName());
        modelTmpl.add("imports", RUNTIME_PKG + ".Model");
        modelTmpl.add("modelFields", im.getBaseModel().getFields());

        CodeModule module = new CodeModule();
        FileObject file = new FileObject();
        file.setFileName(im.getName() + ".java");
        file.setSubPath("src/" + packageName.replace(".", "/"));
        file.setContent(modelTmpl.render());
        module.addFile(file);
        return module;
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
