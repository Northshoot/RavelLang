package org.stanford.ravel;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stanford.antlr4.RavelLexer;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.analysis.*;
import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.compiler.CompileError;
import org.stanford.ravel.compiler.DefPhase;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ValidateScope;
import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.InterfaceSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.error.FatalCompilerErrorException;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.Space;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        ANTLRInputStream input = new ANTLRInputStream(is);

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
        ModelCompiler compiler = new ModelCompiler(this, options.hasFOption("dump-models"));
        for (ModelSymbol m : scope.getModels()) {
            app.addModel(m.getName(), compiler.compile(m));
        }
    }

    private void compileInterfaces(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        InterfaceCompiler compiler = new InterfaceCompiler(this);
        for (InterfaceSymbol isym : scope.getInterfaces()) {
            app.addInterface(isym.getName(), compiler.compileInterface(isym));
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

    private void compileSpaces(GlobalScope scope, RavelApplication app) throws FatalCompilerErrorException {
        // this is effectively the ref/link phase, where
        // models, controllers, and platforms are linked together
        ModelControllerLinker linker = new ModelControllerLinker(this, app);

        for (SpaceSymbol spaceSym : scope.getSpaces()) {
            Space s = linker.processSpace(spaceSym);
            if (s != null)
                app.addSpace(spaceSym.getName(), s);
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
            System.err.println("Build path " + options.getBuildPath());

            try {
                ParseTree tree;

                try {
                    InputStream is = new FileInputStream(options.getInputPath());
                    tree = parse(is);
                } catch (IOException e) {
                    System.err.println("Failed to read input file: " + e.getLocalizedMessage());
                    return;
                }
                if (!success())
                    return;

                // define (hoist) the models and controllers
                GlobalScope globalScope = defPhase(tree);
                if (!success())
                    return;
                ValidateScope.validate(globalScope);

                RavelApplication app = new RavelApplication();

                // typecheck the models, assign types to the fields
                compileModels(globalScope, app);
                if (!success())
                    return;

                // typecheck the interfaces, find implementations
                compileInterfaces(globalScope, app);
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
                SecurityAnalysis securityAnalysis = new SecurityAnalysis(this, app, options.hasFOption("debug-security-analysis"));
                securityAnalysis.run();
                if (!success())
                    return;

                // lower IR and prepare for code generation
                compileControllersPostAnalysis(app);

                LOGGER.info("Internal representation is created!");

                generateCode(app);
            } catch (FatalCompilerErrorException e) {
                // do nothing, this exception is just a quick way to interrupt compilation
                // if something really bad happens
            } finally {
                logBuildEnd();
            }
        } catch(InvalidOptionException e) {
            System.err.println(e.getMessage());
            System.err.println();
            options.help();
        }
    }

    public static void main(String[] args) {
        RavelCompiler driver = new RavelCompiler();
        driver.run(args);
    }

}
