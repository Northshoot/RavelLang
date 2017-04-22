package edu.stanford.ravel.compiler;

import edu.stanford.antlr4.RavelBaseListener;
import edu.stanford.antlr4.RavelParser;
import edu.stanford.ravel.RavelCompiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 4/21/17.
 */
public class ImportPhase  extends RavelBaseListener {

    List<String> importList = new ArrayList<>();

    private final boolean debug;

    private final RavelCompiler driver;

    public ImportPhase(RavelCompiler driver, boolean debug) {
        this.driver = driver;
        this.debug = debug;
    }
    @Override
    public void enterImportStatement(RavelParser.ImportStatementContext ctx) {
        //TODO: we import everything now
        RavelParser.Import_nameContext impt_by_name = ctx.import_stmt().import_name();
        RavelParser.Import_nameContext impt_from = ctx.import_stmt().import_name();
        if(impt_from.isEmpty()){
            System.err.println("Not supported import from");
        } else {
            System.out.println("regular import");
        }
    }
    @Override public void enterDottedName(RavelParser.DottedNameContext ctx) {
        System.out.println(ctx.getText());
    }
    @Override
    public void exitImportStatement(RavelParser.ImportStatementContext ctx) {
        //TODO: load the files

    }

    public List<String> getImportList() {
        return importList;
    }

    public class Import {
        public String from = "";
        public String as = "";
        public String name = "";

        public Import() {}
    }
}
