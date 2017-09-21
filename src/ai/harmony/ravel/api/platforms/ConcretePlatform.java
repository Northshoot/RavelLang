package ai.harmony.ravel.api.platforms;

import ai.harmony.ravel.api.builder.FileObject;
import ai.harmony.ravel.api.lang.ConcreteLanguage;
import ai.harmony.ravel.primitives.Space;

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
