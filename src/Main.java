import ai.harmony.antlr4.RavelLexer;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.api.builder.PlatformBuilder;
import ai.harmony.ravel.PrimitiveRepPhase;
import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.compiler.DefPhase;
import ai.harmony.ravel.compiler.RefPhase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;


public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        String mBuildPath = null;
        System.out.println("Starting Build");
        Date t = Calendar.getInstance().getTime();
        long start = t.getTime();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(t));
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
        //mBuildPath += "/src/";

        ANTLRInputStream input = new ANTLRInputStream(is);

        RavelLexer lexer = new RavelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RavelParser parser = new RavelParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file_input();
        ParseTreeWalker walker = new ParseTreeWalker();


        /**
         * First create Defined symbols
         */
        DefPhase def = new DefPhase();
        walker.walk(def, tree);

        /**
         * Second create reference symbols and resolve definitions
         * What do we want to validate?
         * Added: 18/8/2016 09:06:04 For a starter:
         * (1) Variable references have corresponding definitions in the scope
         * (2) Event declarations have corresponding definitions in the scope
         * (3) Variables are not used as functions
         * (4) Functions are not used as variables
         * (5) Referenced functions context has definitions in it
         * (6) Components are using appropriate component blocks
         * (7) Models, Controller and views are instantiated on to a particular space
         * (8) components are instantiated with allowed keywords
         * (9) schema fields have required and allowed keywords, parameters
         * (10) Model fields are accessed with correct privileges that are defined in model properties
         *
         *
         */
        //TODO: this is just a sample testing if models and controllers are defined and
        //if model scopes are defined
        RefPhase ref = new RefPhase(def.globalScope);
        walker.walk(ref, tree);
        /**
         *  Morph to an complete internal representation
         *  Create concrete instances of models
         *
         */
        RavelApplication rApp = new RavelApplication();
        PrimitiveRepPhase inter = new PrimitiveRepPhase(def.globalScope, rApp);
        walker.walk(inter, tree);
        rApp.link();
        LOGGER.info("Internal representation is created!");
        /**
         *Generate code
         */
        //TODO: need settings file
        PlatformBuilder builder = PlatformBuilder.getInstance();
        builder.setApp(rApp);
        builder.setPath(mBuildPath);
        builder.buildAll();
        builder.render();

//        }
        Date now = Calendar.getInstance().getTime();
        long diff = now.getTime() - start;
        long diffMilliSeconds = diff % 60;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        System.out.println("Build finished at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                " in " + diffMinutes + ":" + diffSeconds + ":" + diffMilliSeconds);

    }

}
