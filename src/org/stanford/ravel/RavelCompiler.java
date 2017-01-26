package org.stanford.ravel;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stanford.antlr4.RavelLexer;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.api.builder.PlatformBuilder;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stanford.ravel.compiler.CompileError;
import org.stanford.ravel.compiler.ControllerEventCompiler;
import org.stanford.ravel.compiler.DefPhase;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.EventSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.error.FatalCompilerErrorException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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
    private final List<CompileError> errors = new ArrayList<>();

    public boolean success() {
        return !hadErrors;
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
        DefPhase listener = new DefPhase(this);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        return listener.getGlobalScope();
    }

    private void compileModels(GlobalScope app, ModelIR mir) throws FatalCompilerErrorException {
        for (ModelSymbol m : app.getModels()) {
            mir.addModel(m);
        }
    }

    private void compileControllers(GlobalScope app) throws FatalCompilerErrorException {
        ControllerEventCompiler compiler = new ControllerEventCompiler(this);

        for (ControllerSymbol c : app.getControllers()) {
           for (EventSymbol event : c.getEvents()) {
               compiler.compileEvent((RavelParser.EventScopeContext) event.getDefNode());
           }
        }
    }

    private void compileSpaces(GlobalScope app) throws FatalCompilerErrorException {
        // this is effectively the ref/link phase, where
        // models, controllers, and platforms are linked together

        for (SpaceSymbol s : app.getSpaces()) {
            // for (Controller c : s.getControllers()) {
            //    s.addInstantiatedController(c.instantiate(s));
            // }
        }
    }

    private void generateCode(RavelApplication app, String buildPath) {
        PlatformBuilder builder = new PlatformBuilder(app, buildPath);
        builder.buildAll();
        builder.render();
    }

    private void run(String inputFile) {
        logBuildStart();

        try {
            String mBuildPath = null;
            InputStream is = System.in;

            ParseTree tree;

            try {
                if (inputFile != null) {
                    is = new FileInputStream(inputFile);
                    mBuildPath = Paths.get(inputFile).toAbsolutePath().getParent().toString();
                    mBuildPath += "/rout/";
                } else {
                    mBuildPath = "./rout/";
                }
                System.err.println("Build path " + mBuildPath);

                tree = parse(is);
            } catch(IOException e) {
                System.err.println("Failed to read input file: " + e.getLocalizedMessage());
                return;
            }

            // define (hoist) the models and controllers
            GlobalScope globalScope = defPhase(tree);
            if (!success())
                return;

            // typecheck the models, assign types to the
            ModelIR mir = new ModelIR();
            compileModels(globalScope, mir);
            if (!success())
                return;

            // compile the controllers to IR
            compileControllers(globalScope);
            if (!success())
                return;

            // link controllers, models and platforms
            compileSpaces(globalScope);
            if (!success())
                return;

            LOGGER.info("Internal representation is created!");

            RavelApplication app = new RavelApplication();
            generateCode(app, mBuildPath);
        } catch (FatalCompilerErrorException e) {
            // do nothing, this exception is just a quick way to interrupt compilation
            // if something really bad happens
        } finally {
            logBuildEnd();
        }
    }

    public static void main(String[] args) {
        RavelCompiler driver = new RavelCompiler();

        driver.run(args.length > 0 ? args[0] : null);
    }

}
