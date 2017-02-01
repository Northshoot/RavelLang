package org.stanford.ravel.api.builder;

import org.stanford.ravel.api.builder.FileObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A collection of code files that implement a related function, eg a .c and associated .h
 * file.
 *
 * Created by gcampagn on 1/27/17.
 */
public class CodeModule {
    private final List<FileObject> files = new ArrayList<>();

    public void addFile(FileObject fo) {
        files.add(fo);
    }

    public void setSubPath(String subPath) {
        for (FileObject fo : files)
            fo.setSubPath(subPath);
    }

    public Collection<FileObject> getFiles() {
        return files;
    }
}
