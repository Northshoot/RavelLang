package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public interface ConcreteLanguage {
    OptionParser getOptions();

    List<FileObject> build(Space s);
}
