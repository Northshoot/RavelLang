package edu.stanford.ravel.api.lang;

import edu.stanford.ravel.api.OptionParser;
import edu.stanford.ravel.api.Settings;
import edu.stanford.ravel.api.builder.CodeModule;
import edu.stanford.ravel.api.builder.FileObject;
import edu.stanford.ravel.api.lang.java.JavaLanguageOptions;
import edu.stanford.ravel.compiler.ir.typed.*;
import edu.stanford.ravel.compiler.types.*;
import edu.stanford.ravel.primitives.*;
import edu.stanford.ravel.compiler.ir.BinaryOperation;
import edu.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.logging.Logger;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class JLang extends BaseLanguage {
    private static Logger LOGGER = Logger.getLogger(JLang.class.getName());
    private final static String BASE_LANG_TMPL_PATH = Settings.BASE_TMPL_PATH +"/lang/java/tmpl";
    private final static String RUNTIME_PKG = "org.stanford.ravel.rrt";

    private static boolean typeIsIteratorInstance(Type type) {
        if (!(type instanceof ClassType.InstanceType))
            return false;

        ClassType owner = ((ClassType.InstanceType) type).getClassType();
        return owner instanceof ModelType.IteratorType;
    }

    private static final AttributeRenderer JTYPES = new AttributeRenderer() {
        private String toNativeType(Type type) {
            if (type instanceof PrimitiveType) {
                switch ((PrimitiveType) type) {
                    case STR:
                        return "java.lang.String";
                    case ERROR_MSG:
                        return RUNTIME_PKG + ".tiers.Error";
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
                        return "Object"; // FIXME
                    case ERROR:
                    default:
                        throw new AssertionError();
                }
            } else if (type instanceof ArrayType) {
                return toNativeType(((ArrayType) type).getElementType()) + "[]";
            } else if (type == IntrinsicTypes.ENDPOINT.getInstanceType()) {
                return RUNTIME_PKG + ".tiers.Endpoint";
            } else if (typeIsIteratorInstance(type)) {
                ClassType classType = ((ClassType.InstanceType)type).getClassType();
                ModelType modelType = ((ModelType.IteratorType)classType).getOwner();
                return "java.util.Iterator<" + modelType.getName() + ".Record>";
            } else if (type instanceof ClassType.InstanceType) {
                return ((ClassType.InstanceType) type).getClassType().getName();
            } else if (type instanceof ModelType.RecordType) {
                return ((ModelType.RecordType) type).getModel().getName() + ".Record";
            } else if (type instanceof ModelType.ContextType) {
                return RUNTIME_PKG + ".Context<" + toNativeType(((ModelType.ContextType) type).getOwner()) + ".Record>";
            } else if (type instanceof SystemType) {
                return RUNTIME_PKG + ".DispatcherAPI";
            } else if (type == IntrinsicTypes.KEY) {
                return "java.security.Key";
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
    private static final AttributeRenderer JSTRING = new AttributeRenderer() {
        @Override
        public String toString(Object o, String s, Locale locale) {
            if (s == null)
                return o.toString();

            switch (s) {
                case "literal":
                    return JLITERAL.toLiteral(o);
                default:
                    return o.toString();
            }
        }
    };

    private final STGroup controllerGroup;
    private final STGroup modelGroup;
    private final STGroup irGroup;
    private final IRTranslator irTranslator;
    private final STGroup dispatcherGroup;
    private final STGroup viewGroup;

    public JLang() {
        dispatcherGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/dispatcher.stg");
        dispatcherGroup.registerRenderer(Type.class, JTYPES);
        dispatcherGroup.registerRenderer(String.class, JSTRING);

        irGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/ir.stg");
        irGroup.registerRenderer(Type.class, JTYPES);
        irGroup.registerRenderer(String.class, JSTRING);

        // quirks for Java implemented as a nested class
        irTranslator = new STIRTranslator(irGroup, JLITERAL) {
            // override declare controller scope to add "this.", so that references in the code
            // are correct always
            @Override
            protected void declareControllerScope(VariableSymbol sym) {
                setRegisterName(sym.getRegister(), "this." + sym.getName());
            }

            /**
             * Quirk the conversion of ERROR_MSG and pointers to bool
             *
             * (True iff error != SUCCESS)
             *
             * @param convert the conversion operation
             */
            @Override
            public void visit(TConvert convert) {
                if (convert.tgtType == PrimitiveType.BOOL) {
                    if (convert.srcType == PrimitiveType.ERROR_MSG)
                        addLine(convert.target, " = ", convert.source, " != null && ", convert.source, " != ", RUNTIME_PKG + ".tiers.Error.SUCCESS");
                    else if (!(convert.srcType instanceof PrimitiveType))
                        addLine(convert.target, " = ", convert.source, " != null");
                    else
                        super.visit(convert);
                } else if (convert.srcType == PrimitiveType.BYTE) {
                    if (convert.tgtType == PrimitiveType.STR) {
                        addLine(convert.target, " = Integer.toString((int)", convert.source, " & 0xFF)");
                    } else {
                        addLine(convert.target, " = (", JTYPES.toString(convert.tgtType, null, Locale.getDefault()), ") ((int)", convert.source, " & 0xFF)");
                    }
                } else if (convert.tgtType == PrimitiveType.STR) {
                    if (convert.srcType == PrimitiveType.ERROR_MSG) {
                        addLine(convert.target, " = ", convert.source, " == null ? \"success\" : ", convert.source, ".name().toLowerCase()");
                    } else {
                        addLine(convert.target, " = ", (convert.srcType == PrimitiveType.INT32 ? "Integer" : "Double"), ".toString(", convert.source, ")");
                    }
                } else {
                    super.visit(convert);
                }
            }

            /**
             * Quirk certain binary operators:
             *
             * - IDIV for doubles is quirked to Math.floor(src1/src2)
             * - IDIV for ints is quirked to src1/src2
             *   (otherwise we would output src1//src2 which is not valid Java)
             * - POW is quirked to Math.pow(src1, src2)
             * - bitwise operators on bytes require a cast to byte because they implicity promote to int
             */
            @Override
            public void visit(TBinaryArithOp arithOp) {
                if (arithOp.op == BinaryOperation.IDIV) {
                    if (arithOp.type == PrimitiveType.DOUBLE) {
                        addLine(arithOp.target, " = java.lang.Math.floor(", arithOp.src1, " / ", arithOp.src2, ")");
                    } else {
                        addLine(arithOp.target, " = ", arithOp.src1, " / ", arithOp.src2);
                    }
                } else if (arithOp.op == BinaryOperation.POW) {
                    addLine(arithOp.target, " = java.lang.Math.pow(", arithOp.src1, ", ", arithOp.src2, ")");
                } else if (arithOp.op.isBitwise() && arithOp.type == PrimitiveType.BYTE) {
                    addLine(arithOp.target, "= (byte) (", arithOp.src1, " ", arithOp.op, " ", arithOp.src2, ")");
                } else {
                    super.visit(arithOp);
                }
            }

            /**
             * Quirk certain unary operators:
             *
             * - bitwise operators on bytes require a cast to byte because they implicitly promote to int
             */
            @Override
            public void visit(TUnaryArithOp arithOp) {
                if (arithOp.op.isBitwise() && arithOp.type == PrimitiveType.BYTE) {
                    addLine(arithOp.target, "= (byte) (", arithOp.op, " ", arithOp.source, ")");
                } else {
                    super.visit(arithOp);
                }
            }

            /**
             * Quirk comparisons:
             *
             * - string comparisons require a call to str1.compareTo(str2) op 0
             *   (eg str1.compareTo(str2) > 0
             *
             */
            @Override
            public void visit(TComparisonOp compOp) {
                if (compOp.type == PrimitiveType.STR) {
                    addLine(compOp.target, " = ", compOp.src1, ".compareTo(", compOp.src2, ") ", compOp.op, " 0");
                } else {
                    super.visit(compOp);
                }
            }
        };

        controllerGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/controller.stg");
        controllerGroup.registerRenderer(Type.class, JTYPES);
        controllerGroup.registerRenderer(String.class, JSTRING);
        controllerGroup.registerRenderer(TypedIR.class, (Object o, String s, Locale locale) -> {
            TypedIR ir = (TypedIR)o;
            irTranslator.translate(ir);
            return irTranslator.getCode();
        });

        modelGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/model.stg");
        modelGroup.registerRenderer(Type.class, JTYPES);
        modelGroup.registerRenderer(String.class, JSTRING);
        modelGroup.registerRenderer(TypedIR.class, (Object o, String s, Locale locale) -> {
            TypedIR ir = (TypedIR)o;
            irTranslator.translate(ir);
            return irTranslator.getCode();
        });

        viewGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/view.stg");
        viewGroup.registerRenderer(Type.class, JTYPES);
        viewGroup.registerRenderer(String.class, JSTRING);
        viewGroup.registerRenderer(TypedIR.class, (Object o, String s, Locale locale) -> {
            TypedIR ir = (TypedIR)o;
            irTranslator.translate(ir);
            return irTranslator.getCode();
        });
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
    private class Pair<A, B> {
        public final A first;
        public final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }

    @Override
    protected CodeModule createDispatcher(Space s) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST tmpl = dispatcherGroup.getInstanceOf("file");
        String packageName = options.getPackageName();

        for (ConcreteModel im : s.getModels())
            tmpl.add("imports", packageName + ".models." + im.getName());
        for (ConcreteInterface iiface : s.getInterfaces())
            tmpl.add("imports", packageName + ".interfaces." + iiface.getName());
        for (ConcreteView iview : s.getViews())
            tmpl.add("imports", packageName + ".views." + iview.getName());
        for (edu.stanford.ravel.primitives.ConcreteController ictr : s.getControllers())
            tmpl.add("imports", packageName + ".controller." + ictr.getName());

        for (ConcreteModelInstance im : s.getModelInstances())
            tmpl.add("models", im);
        for (ConcreteInterfaceInstance iiface : s.getInterfaceInstances())
            tmpl.add("interfaces", iiface);

        Map<String, List<Pair<String, String>>> viewAssignments = new HashMap<>();
        for (ConcreteViewInstance iview : s.getViewInstances()) {
            tmpl.add("views", iview);
            viewAssignments.put(iview.getVarName(), new ArrayList<>());
        }
        tmpl.add("viewAssignments", viewAssignments);


        for (ConcreteControllerInstance ictr : s.getControllerInstances()) {
            ConcreteController concrete = new ConcreteController();
            concrete.name = ictr.getComponent().getName();
            concrete.varName = ictr.getVarName();

            for (VariableSymbol sym : ictr.getComponent().getController().getParameterSymbols()) {
                Object pvalue = ictr.getParam(sym.getName());

                if (pvalue instanceof ConcreteModelInstance) {
                    concrete.parameterValues.add("model_" + ((ConcreteModelInstance) pvalue).getVarName());
                } else if (pvalue instanceof ConcreteInterfaceInstance) {
                    concrete.parameterValues.add("iface_" + ((ConcreteInterfaceInstance) pvalue).getVarName());
                } else if (pvalue instanceof ConcreteViewInstance) {
                    concrete.parameterValues.add("null");
                    viewAssignments.get(((ConcreteViewInstance) pvalue).getVarName()).add(new Pair<>(ictr.getVarName(), sym.getName()));
                } else if (pvalue instanceof SystemAPIInstance) {
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
    public CodeModule createInterface(ConcreteInterface iiface) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();

        String ifaceGroupName = iiface.getBaseInterface().getImplementation("java");
        if (ifaceGroupName == null) {
            LOGGER.severe("Missing Java implementation of " + iiface.getName());
            return null;
        }

        STGroupFile ifaceGroup = new STGroupFile(ifaceGroupName);
        ifaceGroup.registerRenderer(Type.class, JTYPES);
        ifaceGroup.registerRenderer(String.class, JSTRING);
        ST ifaceTmpl = ifaceGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".interfaces";
        ifaceTmpl.add("imports", options.getPackageName() + ".AppDispatcher");
        for (ConcreteControllerInstance ictr : iiface.getControllerList())
            ifaceTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getComponent().getName());
        for (Model model : iiface.getBaseInterface().getModels().values())
            ifaceTmpl.add("imports", options.getPackageName() + ".models." + model.getName());
        ifaceTmpl.add("interface", iiface);

        return simpleModule(ifaceTmpl, iiface.getName(), packageName);
    }

    @Override
    public CodeModule createView(ConcreteView iview) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();
        ST viewTmpl = viewGroup.getInstanceOf("file");

        String packageName = options.getPackageName() + ".views";
        viewTmpl.add("imports", options.getPackageName() + ".AppDispatcher");
        for (ConcreteControllerInstance ictr : iview.getControllerList())
            viewTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getComponent().getName());
        for (Model model : iview.getBaseView().getModels().values())
            viewTmpl.add("imports", options.getPackageName() + ".models." + model.getName());
        viewTmpl.add("view", iview);

        return simpleModule(viewTmpl, iview.getName(), packageName);
    }

    @Override
    protected CodeModule createModel(ConcreteModel im) {
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
        for (ConcreteControllerInstance ictr : im.getControllerList())
            modelTmpl.add("imports", options.getPackageName() + ".controller." + ictr.getComponent().getName());
        modelTmpl.add("base", baseClass);
        modelTmpl.add("model", im);
        modelTmpl.add("set_endpoints", im.getBaseModel().getModelType() != Model.Type.LOCAL);

        return simpleModule(modelTmpl, im.getName(), packageName);
    }

    @Override
    protected CodeModule createController(edu.stanford.ravel.primitives.ConcreteController ictr) {
        JavaLanguageOptions options = JavaLanguageOptions.getInstance();

        ST controllerTmpl = controllerGroup.getInstanceOf("file");
        String packageName = options.getPackageName() + ".controller";
        controllerTmpl.add("package", packageName);
        controllerTmpl.add("name", ictr.getName());

        for (ConcreteModel im : ictr.getLinkedModels()) {
            controllerTmpl.add("imports", options.getPackageName() + ".models." + im.getName());
        }
        for (ConcreteInterface iiface : ictr.getLinkedInterfaces()) {
            controllerTmpl.add("imports", options.getPackageName() + ".interfaces." + iiface.getName());
        }
        for (ConcreteView iview : ictr.getLinkedViews()) {
            controllerTmpl.add("imports", options.getPackageName() + ".views." + iview.getName());
        }

        STControllerTranslator.FileConfig fileConfig = new STControllerTranslator.FileConfig(ictr.getName() + ".java", controllerTmpl);

        STControllerTranslator controllerTranslator = new STControllerTranslator(Collections.singletonList(fileConfig), irTranslator);
        CodeModule generated = controllerTranslator.translate(ictr.getController());
        generated.setSubPath("src/" + packageName.replace(".", "/"));
        return generated;
    }
}
