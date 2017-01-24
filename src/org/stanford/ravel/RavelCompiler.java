package org.stanford.ravel;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stanford.antlr4.RavelLexer;
import org.stanford.antlr4.RavelParser;
import org.stanford.api.builder.PlatformBuilder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stanford.ravel.compiler.ControllerCompiler;
import org.stanford.ravel.compiler.DefPhase;
import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.symbol.ControllerSymbol;
import org.stanford.ravel.compiler.symbol.EventSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.Space;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;


public class RavelCompiler {
    private static Logger LOGGER = Logger.getLogger(RavelCompiler.class.getName());

    private static long logBuildStart() {
        System.out.println("Starting Build");
        Date t = Calendar.getInstance().getTime();
        long start = t.getTime();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(t));
        return start;
    }

    private static void logBuildEnd(long start) {
        Date now = Calendar.getInstance().getTime();
        long diff = now.getTime() - start;
        long diffMilliSeconds = diff % 60;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        System.out.println("Build finished at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                " in " + diffMinutes + ":" + diffSeconds + ":" + diffMilliSeconds);
    }

    private static ParseTree parse(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);

        RavelLexer lexer = new RavelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RavelParser parser = new RavelParser(tokens);
        parser.setBuildParseTree(true);
        return  parser.file_input();
    }

    private static GlobalScope defPhase(ParseTree tree) {
        DefPhase listener = new DefPhase();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        return listener.getGlobalScope();
    }

    private static void compileModels(GlobalScope app, ModelIR mir) {
        for (ModelSymbol m : app.getModels()) {
            mir.addModel(m);
        }
    }

    private static boolean compileControllers(GlobalScope app) {
        ControllerCompiler compiler = new ControllerCompiler();

        boolean success = true;
        for (ControllerSymbol c : app.getControllers()) {
           for (EventSymbol event : c.getEvents()) {
               success = compiler.compileEvent((RavelParser.EventScopeContext)event.getDefNode()) && success;
           }
        }

        compiler.printAllErrors();
        return success;
    }

    private static void compileSpaces(GlobalScope app) {
        // this is effectively the ref/link phase, where
        // models, controllers, and platforms are linked together

        for (SpaceSymbol s : app.getSpaces()) {
            // for (Controller c : s.getControllers()) {
            //    s.addInstantiatedController(c.instantiate(s));
            // }
        }
    }

    private static void generateCode(RavelApplication app, String buildPath) {
        PlatformBuilder builder = PlatformBuilder.getInstance();
        builder.setApp(app);
        builder.setPath(buildPath);
        builder.buildAll();
        builder.render();
    }

    public static void main(String[] args) throws Exception {
        long start = logBuildStart();

        String inputFile = null;
        String mBuildPath = null;
        if (args.length > 0) inputFile = args[0];
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
            mBuildPath = Paths.get(args[0]).toAbsolutePath().getParent().toString();
            mBuildPath+="/rout/";
            System.out.println("Build path " + mBuildPath);
        } else {
            System.out.println("File is null");
        }

        ParseTree tree = parse(is);

        // define (hoist) the models and controllers
        GlobalScope globalScope = defPhase(tree);
        // typecheck the models, assign types to the
        ModelIR mir = new ModelIR();
        compileModels(globalScope, mir);

        // compile the controllers to IR
        if (!compileControllers(globalScope)) {
            logBuildEnd(start);
            return;
        }

        // link controllers, models and platforms
        compileSpaces(globalScope);

        LOGGER.info("Internal representation is created!");

        RavelApplication app = new RavelApplication();
        generateCode(app, mBuildPath);

        logBuildEnd(start);
    }

}
