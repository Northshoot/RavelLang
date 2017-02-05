package org.stanford.ravel;

import org.stanford.ravel.api.InvalidOptionException;
import org.stanford.ravel.api.OptionParser;
import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.ConcreteLanguage;
import org.stanford.ravel.api.platforms.BasePlatform;
import org.stanford.ravel.primitives.Platform;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lauril on 10/6/16.
 */
class PlatformBuilder {
    private final RavelApplication rApp;
    private final List<FileObject> mFiles; //collect all the files to generate

    private String path;

    PlatformBuilder(RavelApplication rApp) {
        this.rApp = rApp;
        mFiles = new ArrayList<>();
    }

    void applyOptions(RavelOptionParser options) throws InvalidOptionException {
        path = options.getBuildPath();

        Set<OptionParser> parsers = new HashSet<>();
        for (Space s : rApp.getSpaces()) {
            Platform platform = s.getPlatform();
            ConcreteLanguage lang = platform.getConcreteLanguage();
            parsers.add(lang.getOptions());
        }

        for (OptionParser op : parsers) {
            options.applyXOptions(op);
        }
    }

    void buildAll() {
        for(Space s : rApp.getSpaces()){
            buildSpace(s);
        }
    }

    private void buildSpace(Space s) {
        System.out.println("Building Space: \n" + s.getName());

        Platform platform = s.getPlatform();
        ConcreteLanguage lang = platform.getConcreteLanguage();
        BasePlatform concretePlatform = platform.getConcretePlatform();

        String path = this.path + s.getName();
        List<FileObject> spaceFiles = new ArrayList<>();
        spaceFiles.addAll(lang.build(s));
        spaceFiles.addAll(concretePlatform.build(s));
        for (FileObject fo : spaceFiles)
            fo.setBasePath(path);
        mFiles.addAll(spaceFiles);
    }

    void render() {
        for (FileObject fo : mFiles) {
            fo.toFile();
        }
    }
}
