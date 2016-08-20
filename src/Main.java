
import ai.harmony.ravel.antlr4.*;
import ai.harmony.ravel.compiler.DefPhase;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

;

public class Main {


    public static void main(String[] args) throws Exception {
        String inputFile = null;
        System.out.println("Starting Build");
        Date t = Calendar.getInstance().getTime();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(t));
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        } else {
            System.out.println("File is null");
        }
        ANTLRInputStream input = new ANTLRInputStream(is);

        RavelLexer lexer = new RavelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RavelParser parser = new RavelParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file_input();
        ParseTreeWalker walker = new ParseTreeWalker();
        DefPhase def = new DefPhase();
        walker.walk(def,tree);

//        IR ir = new IR();
//        PrimitiveVisitor converter = new PrimitiveVisitor(ir);
//        Void result = converter.visit(tree);

        /**
         * First create Defined symbols
         */

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

        /**
         *  Morph to an complete internal representation
         *
         * /

         /**
         *Generate code
         */

        Date now = Calendar.getInstance().getTime();
        long diff = now.getTime() -t.getTime();
        long diffMilliSeconds = diff  % 60;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        System.out.println("Build finished at " + new SimpleDateFormat("HH:mm:ss").format(now) +
                        " in " + diffMinutes + ":" + diffSeconds + ":" +diffMilliSeconds);

        }

}
