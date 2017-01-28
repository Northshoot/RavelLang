package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.logging.Logger;

import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * Class that is responsible for creating a particular language representation
 * <p>
 * Created by lauril on 10/6/16.
 */
public class PyLang extends ConcreteLanguage{
    private static Logger LOGGER = Logger.getLogger(PyLang.class.getName());
    public final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/lang/python/tmpl";
    Space mSpace;
    STGroup model_tmpl;

    public PyLang() {
        super();
        LOGGER.severe("Building Python Language");
        //model_tmpl = new STGroupFile(BASE_LANG_TMPL_PATH +"/model.stg");
    }


    @Override
    public List<FileObject> build(Space s, String buildPath) {
        LOGGER.info("Building Space: " +s.mName);
        return mFileObjects;
    }

    private void createModels() {



    }



    private void createControllers() {
    }


}