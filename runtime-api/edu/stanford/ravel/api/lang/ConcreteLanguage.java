package edu.stanford.ravel.api.lang;

import edu.stanford.ravel.api.OptionParser;
import edu.stanford.ravel.primitives.Space;
import edu.stanford.ravel.api.builder.FileObject;

import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public interface ConcreteLanguage {
    OptionParser getOptions();

    List<FileObject> build(Space s);
}
