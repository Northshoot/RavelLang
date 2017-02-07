package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.primitives.Space;

import java.util.List;

/**
 * Created by gcampagn on 1/31/17.
 */
public interface ConcretePlatform {
    OptionParser getOptions();

    boolean allowsLanguage(ConcreteLanguage lang);

    List<FileObject> build(Space s);

    List<FileObject> createBuildSystem(Space s, List<FileObject> files);

    void setAPILevel(int level);
}
