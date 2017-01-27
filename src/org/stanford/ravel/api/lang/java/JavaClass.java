package org.stanford.ravel.api.lang.java;

import org.stanford.ravel.api.builder.FileObject;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * A single java class, could be a model, a controller, or
 * the app.
 *
 * Created by gcampagn on 1/26/17.
 */
public abstract class JavaClass {
    final STGroup template;
    private final String name;
    private final String packageName;

    JavaClass(String name, String packageName, STGroup template) {
        this.name = name;
        this.packageName = packageName;
        this.template = template;
    }

    abstract ST prepareFile();

    public FileObject render() {
        FileObject fo = new FileObject();
        fo.setFileName(name + ".java");

        ST file = prepareFile();
        file.add("package", packageName);
        file.add("className", name);
        fo.setContent(file.render());
        return fo;
    }
}
