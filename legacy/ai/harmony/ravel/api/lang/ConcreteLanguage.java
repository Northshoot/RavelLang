package ai.harmony.ravel.api.lang;

import ai.harmony.ravel.api.OptionParser;
import ai.harmony.ravel.api.builder.FileObject;
import ai.harmony.ravel.primitives.Space;

import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public interface ConcreteLanguage {
    OptionParser getOptions();

    List<FileObject> build(Space s);
}
