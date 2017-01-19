package org.stanford.ravel;

import org.stanford.antlr4.RavelLexer;
import org.stanford.antlr4.RavelParser;
import org.stanford.api.builder.PlatformBuilder;
import org.stanford.ravel.PrimitiveRepPhase;
import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.compiler.DefPhase;
import org.stanford.ravel.compiler.RefPhase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;


public class RavelCompiler {
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static void logBuildStart() {
        System.out.println("Starting Build");
        Date t = Calendar.getInstance().getTime();
        long start = t.getTime();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(t));
    }

    private static void logBuildEnd() {
        Date now = Calendar.getInstance().getTime();
        long diff = now.getTime() - start;
        long diffMilliSeconds = diff % 60;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        System.out.println("Build finished at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                " in " + diffMinutes + ":" + diffSeconds + ":" + diffMilliSeconds);
    }

    private static ParseTree parse(InputStream is) {
        ANTLRInputStream input = new ANTLRInputStream(is);

        RavelLexer lexer = new RavelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RavelParser parser = new RavelParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file_input();
    }

    private RavelApplication defPhase(ParseTree tree) {
        RavelApplication rApp = new RavelApplication();

        // TODO

        return rApp;
    }

    private void compileModels(RavelApplication app) {
        for (Model m : app.getModels()) {

        }
    }

    private void compileControllers(RavelApplication app) {
        for (Controller c : app.getControllers()) {
            //
            // ControllerHIR hir = analyzeSyntax(c.getParseTree());
            // ControllerIR ir = typeResolve(hir);
            // ir = transform(ir);
            // ir = lower(ir);
            // c.setTargetIR(ir);
        }
    }

    private void compileSpaces(RavelApplication app) {
        // this is effectively the ref/link phase, where
        // models, controllers, and platforms are linked together

        for (Space s : app.getSpaces()) {
            // for (Controller c : s.getControllers()) {
            //    s.addInstantiatedController(c.instantiate(s));
            // }
        }
    }

    private void generateCode(RavelApplication app, String buildPath) {
        PlatformBuilder builder = PlatformBuilder.getInstance();
        builder.setApp(rApp);
        builder.setPath(buildPath);
        builder.buildAll();
        builder.render();
    }

    public static void main(String[] args) throws Exception {
        logBuildStart();

        InputStream is;
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
        RavelApplication app = defPhase(tree);
        // typecheck the models, assign types to the fields
        compileModels(app);

        // compile the controllers to IR
        compileControllers(app);

        // link controllers, models and platforms
        compileSpaces(app);

        LOGGER.info("Internal representation is created!");

        generateCode();

        logBuildEnd();

    }

}
