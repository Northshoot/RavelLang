
import ai.harmony.ravel.antlr.*;
import ai.harmony.ravel.*;
import ai.harmony.ravel.primitives.PrimitiveListner;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

;

public class Main {


    public static void main(String[] args) throws Exception {
        String inputFile = null;
        System.out.println("Starting Build");
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
        PrimitiveListner converter = new PrimitiveListner();
        walker.walk(converter, tree);
        //System.out.println(converter.getRavel(tree));
        }

}
