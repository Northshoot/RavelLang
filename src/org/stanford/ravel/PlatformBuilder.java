package org.stanford.ravel;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.platforms.ConcretePlatform;
import org.stanford.ravel.primitives.Platform;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public class PlatformBuilder {
    private final RavelApplication rApp;
    private final String path;
    private final List<FileObject> mFiles; //collect all the files to generate

    PlatformBuilder(RavelApplication rApp, String buildPath) {
        this.rApp = rApp;
        this.path = buildPath;
        mFiles = new ArrayList<>();
    }

    public void buildAll(){
        for(Space s : rApp.getSpaces()){
            buildSpace(s);
        }
    }

    private void buildSpace(Space s) {
        System.out.println("Building Space: \n" + s.getName());

        Platform platform = s.getPlatform();
        ConcreteLanguage lang = platform.getConcreteLanguage();
        ConcretePlatform concretePlatform = platform.getConcretePlatform();

        String path = this.path + s.mName;
        mFiles.addAll(lang.build(s));
        mFiles.addAll(concretePlatform.build(s, path));
        for (FileObject fo : mFiles)
            fo.setBasePath(path);
    }

    public void render() {
        for (FileObject fo : mFiles) {
            fo.toFile();
        }
    }
}
