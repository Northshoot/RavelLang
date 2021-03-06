package ai.harmony.ravel;

import ai.harmony.antlr4.RavelLexer;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.director.Publisher;
import ai.harmony.ravel.analysis.FlowAnalysis;
import ai.harmony.ravel.analysis.ModelOperationAnalysis;
import ai.harmony.ravel.analysis.ModelOwnershipAnalysis;
import ai.harmony.ravel.analysis.ModelWritingAnalysis;
import ai.harmony.ravel.analysis.security.SecurityAnalysis;
import ai.harmony.ravel.analysis.security.SecurityTransformation;
import ai.harmony.ravel.api.InvalidOptionException;
import ai.harmony.ravel.compiler.CompileError;
import ai.harmony.ravel.compiler.DefPhase;
import ai.harmony.ravel.compiler.SourceLocation;
import ai.harmony.ravel.compiler.ValidateScope;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.error.FatalCompilerErrorException;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Space;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public class RavelCompiler {
    private static Logger LOGGER = Logger.getLogger(RavelCompiler.class.getName());

    private long start;
    private boolean hadErrors = false;
    private final RavelOptionParser options = new RavelOptionParser();
    private final List<CompileError> errors = new ArrayList<>();
    private ControllerCompiler controllerCompiler;
    private ModelCompiler modelCompiler;

    public String getClassPath() {
        return mClassPath;
    }

    public String getAppPath() {
        return mAppPath;
    }

    //Class path of the compiler
    //TODO: add search for libs
    private String mClassPath;
    //Application path
    private String mAppPath;

    public boolean success() {
        return !hadErrors;
    }

    public void emitInfo(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.INFO, message));
    }
    public void emitError(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.ERROR, message));
        hadErrors = true;
    }
    public void emitWarning(SourceLocation loc, String message) {
        errors.add(new CompileError(loc, CompileError.Severity.WARNING, message));
    }
    public void emitFatal(SourceLocation loc, String message) throws FatalCompilerErrorException {
        errors.add(new CompileError(loc, CompileError.Severity.FATAL, message));
        hadErrors = true;
        throw new FatalCompilerErrorException();
    }

    private void printAllErrors() {
        for (CompileError e : errors)
            System.err.println(e);
    }

    private void logBuildStart() {
        System.err.println("Starting Build");
        Date t = Calendar.getInstance().getTime();
        start = t.getTime();
        System.err.println(new SimpleDateFormat("HH:mm:ss").format(t));
    }

    private void logBuildEnd() {
        printAllErrors();

        Date now = Calendar.getInstance().getTime();
        long diff = now.getTime() - start;
        long diffMilliSeconds = diff % 60;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        if (success()) {
            System.err.println("Build finished at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                    " in " + diffMinutes + ":" + diffSeconds + ":" + diffMilliSeconds);
        } else {
            System.err.println("Build failed at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                    " in " + diffMinutes + ":" + diffSeconds + ":" + diffMilliSeconds);
        }
    }

    private ParseTree parse(InputStream is) throws IOException {
        CharStream input = CharStreams.fromStream(is);

        RavelLexer lexer = new RavelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RavelParser parser = new RavelParser(tokens);
        parser.setBuildParseTree(true);
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                emitError(new SourceLocation(line, charPositionInLine), msg);
            }
        });
        return  parser.file_input();
    }

    private GlobalScope defPhase(ParseTree tree) {
        DefPhase listener = new DefPhase(this, options.hasFOption("dump-scope-tree"));
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
        return listener.getGlobalScope();
    }

    private void compileModels(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        for (ModelSymbol m : scope.getModels()) {
            app.addModel(m.getName(), modelCompiler.compile(m));
        }
    }

    private void compileInterfaces(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        InterfaceCompiler compiler = new InterfaceCompiler(app, this);
        for (InterfaceSymbol isym : scope.getInterfaces()) {
            app.addInterface(isym.getName(), compiler.compileInterface(isym));
        }
    }

    private void compileViews(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        ViewCompiler compiler = new ViewCompiler(app, this);
        for (ViewSymbol isym : scope.getViews()) {
            app.addView(isym.getName(), compiler.compileView(isym));
        }
    }

    private void compileControllersPreAnalysis(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        for (ControllerSymbol c : scope.getControllers()) {
            app.addController(c.getName(), controllerCompiler.preAnalysis(c));
        }
    }

    private void compileControllersPostAnalysis(RavelApplication app) throws FatalCompilerErrorException {
        for (Controller c : app.getControllers())
            controllerCompiler.postAnalysis(c);
    }

    private void compileModelsPostAnalysis(RavelApplication app) throws FatalCompilerErrorException {
        for (Model model : app.getModels())
            modelCompiler.postAnalysis(model);
    }

    private void compileSpaces(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        // this is effectively the ref/link phase, where
        // models, controllers, and platforms are linked together
        ModelControllerLinker linker = new ModelControllerLinker(this, app);

        for (SpaceSymbol spaceSym : scope.getSpaces()) {
            Space s = linker.processSpace(spaceSym);
            if (s != null)
                app.addSpace(spaceSym.getName(), s);
        }

        System.out.println("Spaces/endpoints");
        for (Space s : app.getSpaces()) {
            System.out.println(s);
        }
    }

    private void generateCode(RavelApplication app) throws InvalidOptionException,FatalCompilerErrorException {
        PlatformBuilder builder = new PlatformBuilder(app);
        builder.applyOptions(options);
        builder.buildAll();
        builder.render();
    }

    private void run(String[] args) {
        try {
            options.defaultOn("encrypt");
            options.defaultOn("mac");
            options.parse(args);
            if (options.isHelp()) {
                options.help();
                return;
            }
            if (options.isVersion()) {
                options.version();
                return;
            }

            logBuildStart();
            //TODO: enable to define import path
            String[] path = options.getInputPath().split("/");
            this.mAppPath="";
            for(int i=0;i<path.length-1;i++){
                this.mAppPath+=path[i]+"/";
            }

            try {
                ParseTree tree;
                //get main file
                try {
                    tree = treeFromInput(options.getInputPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (!success())
                    return;

                // define (hoist) the models and controllers
                GlobalScope globalScope = defPhase(tree);
                if (!success())
                    return;
                ValidateScope.validate(globalScope);

                //TODO: need config file
                RavelApplication app = new RavelApplication((path[path.length-1]));

                // typecheck the models, assign types to the fields
                modelCompiler = new ModelCompiler(this, options.hasFOption("dump-models"));
                compileModels(globalScope, app);
                if (!success())
                    return;

                // typecheck the interfaces and views find implementations
                compileInterfaces(globalScope, app);
                compileViews(globalScope, app);
                if (!success())
                    return;

                // compile the controllers to IR
                controllerCompiler = new ControllerCompiler(this, options.hasFOption("dump-ir"));
                compileControllersPreAnalysis(globalScope, app);
                if (!success())
                    return;

                // link controllers, models and platforms
                compileSpaces(globalScope, app);
                if (!success())
                    return;

                // determine who is writing where
                ModelWritingAnalysis writingAnalysis = new ModelWritingAnalysis(this, app);
                writingAnalysis.run();
                if (!success())
                    return;

                // analyze flows
                FlowAnalysis flowAnalysis = new FlowAnalysis(this, app);
                flowAnalysis.run();
                if (!success())
                    return;

                // analyze record creation and map variables to fields
                ModelOwnershipAnalysis ownershipAnalysis = new ModelOwnershipAnalysis(this, app, options.hasFOption("dump-operation-analysis"));
                ownershipAnalysis.run();
                if (!success())
                    return;

                // analyze operations computed on fields
                ModelOperationAnalysis operationAnalysis = new ModelOperationAnalysis(this, app, options.hasFOption("dump-operation-analysis"));
                operationAnalysis.run();
                if (!success())
                    return;

                // assign security primitives to each space
                SecurityAnalysis securityAnalysis = new SecurityAnalysis(this, app, options.hasFOption("debug-security-analysis"), !options.hasFOption("field-level-encryption"), options.hasFOption("homomorphic-encryption"));
                securityAnalysis.run();
                if (!success())
                    return;

                // transform the IR with security info
                SecurityTransformation securityTransformation = new SecurityTransformation(this, app, options.hasFOption("dump-encrypt-ir"), options.hasFOption("encrypt"), options.hasFOption("mac"), !options.hasFOption("field-level-encryption"));
                securityTransformation.run();
                if (!success())
                    return;

                // lower IR and prepare for code generation
                compileControllersPostAnalysis(app);
                compileModelsPostAnalysis(app);

                LOGGER.info("Internal representation is created!");

                generateCode(app);
                //map application to json and push it to the cloud
                System.out.println(app.toJsonString());
                Publisher p = new Publisher();
                String response = p.post(app.toJsonString());

                JsonReader reader = Json.createReader(new StringReader(response));
                JsonObject responseObject = reader.readObject();
                reader.close();
                String new_app_id = responseObject.getString("app.id");
                String old_app_id = RavelProperties.getInstance().getID();

                if (new_app_id.equals(old_app_id)){
                    System.out.println("Successfully updated application");
                } else {
                    RavelProperties.getInstance().setProperty("app.id", new_app_id);
                    System.out.println("Successfully created new application with ID: " + new_app_id);

                }


            } catch (FatalCompilerErrorException e) {
                // do nothing, this exception is just a quick way to interrupt compilation
                // if something really bad happens
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                logBuildEnd();
            }
        } catch(InvalidOptionException e) {
            System.err.println(e.getMessage());
            System.err.println();
            options.help();
        }
    }

    public ParseTree treeFromInput(String path) throws IOException{
            ParseTree tree;

        try {
            InputStream is = new FileInputStream(path);
            tree = parse(is);
            return tree;
        } catch (IOException e) {
            throw new IOException("Failed to read input file: " + e.getLocalizedMessage());

        }

    }

    public static void main(String[] args) {
        System.out.println();
        RavelCompiler driver = new RavelCompiler();
        driver.run(args);
    }

}
