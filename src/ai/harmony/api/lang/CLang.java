package ai.harmony.api.lang;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.ConcretePlatform;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ai.harmony.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class CLang extends ConcreteLanguage{
    private static Logger LOGGER = Logger.getLogger(CLang.class.getName());
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/c/tmpl";
    Space mSpace;
    STGroup model_tmpl;

    public CLang() {
        super();
        model_tmpl = new STGroupFile(BASE_LANG_TMPL_PATH +"/model.stg");
    }



    @Override
    public List<FileObject> build(Space s){
        LOGGER.info("Building Space: " +s.mName);
        mSpace = s;
        createModels();
        createControllers();
        return mFileObjects;
    }

    private void createModels() {

        ST model_header = model_tmpl.getInstanceOf("models_header_file");
        model_header.add("space", mSpace);
        FileObject f = new FileObject();
        f.setPath(mSpace.getBuildPath());
        f.setFileName("model.h");
        f.setContent(model_header.render());
        mFileObjects.add(f);
        ST model_object = model_tmpl.getInstanceOf("models_obj_file");
        model_object.add("space", mSpace);
//        System.out.println(model_object.render());
        f = new FileObject();
        f.setPath(mSpace.getBuildPath());
        f.setFileName("model.c");
        f.setContent(model_object.render());
        mFileObjects.add(f);

    }



    private void createControllers() {
    }


}
