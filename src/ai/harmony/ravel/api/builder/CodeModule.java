package ai.harmony.ravel.api.builder;

import java.util.*;

/**
 * A collection of code files that implement a related function, eg a .c and associated .h
 * file.
 *
 * Created by gcampagn on 1/27/17.
 */
public class CodeModule {
    private final List<FileObject> files = new ArrayList<>();
    public final Map<String, String> buildSystemMeta = new HashMap<>();

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
