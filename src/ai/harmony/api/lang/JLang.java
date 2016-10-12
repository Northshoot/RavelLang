package ai.harmony.api.lang;

import ai.harmony.api.builder.FileObject;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.logging.Logger;

import static ai.harmony.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class JLang extends ConcreteLanguage{
    private static Logger LOGGER = Logger.getLogger(JLang.class.getName());
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/java/tmpl";
    Space mSpace;
    STGroup model_tmpl;

    public JLang() {
        super();
        LOGGER.severe("Building Java Language");
        //model_tmpl = new STGroupFile(BASE_LANG_TMPL_PATH +"/model.stg");
    }



    @Override
    public List<FileObject> build(Space s){
        LOGGER.info("Building Space: " +s.mName);
        return mFileObjects;
    }

    private void createModels() {



    }



    private void createControllers() {
    }


}
