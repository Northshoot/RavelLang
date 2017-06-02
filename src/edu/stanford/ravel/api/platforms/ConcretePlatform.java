package edu.stanford.ravel.api.platforms;

import edu.stanford.ravel.api.builder.FileObject;
import edu.stanford.ravel.api.lang.ConcreteLanguage;
import edu.stanford.ravel.primitives.Space;

import java.util.List;

/**
 * Created by gcampagn on 1/31/17.
 */
public interface ConcretePlatform {
    PlatformOptions getOptions();

    boolean allowsLanguage(ConcreteLanguage lang);

    List<FileObject> build(Space s);

    List<FileObject> createBuildSystem(Space s, List<FileObject> files);

    /**
     * set level of API
     * @param level
     */
    void setAPILevel(int level);

}
