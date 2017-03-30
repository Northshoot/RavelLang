package edu.stanford.ravel;

import edu.stanford.ravel.api.OptionParser;
import edu.stanford.ravel.primitives.Platform;
import edu.stanford.ravel.api.InvalidOptionException;
import edu.stanford.ravel.api.builder.FileObject;
import edu.stanford.ravel.api.lang.ConcreteLanguage;
import edu.stanford.ravel.api.platforms.ConcretePlatform;
import edu.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by lauril on 10/6/16.
 */
class PlatformBuilder {
    private static Logger LOGGER = Logger.getLogger(PlatformBuilder.class.getName());
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
            ConcretePlatform plat = platform.getConcretePlatform();
            parsers.add(lang.getOptions());
            parsers.add(plat.getOptions());
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
        Platform platform = s.getPlatform();
        ConcreteLanguage lang = platform.getConcreteLanguage();
        ConcretePlatform concretePlatform = platform.getConcretePlatform();

        String path = this.path + s.getName();
        List<FileObject> spaceFiles = new ArrayList<>();
        spaceFiles.addAll(lang.build(s));
        spaceFiles.addAll(concretePlatform.build(s));
        spaceFiles.addAll(concretePlatform.createBuildSystem(s, spaceFiles));
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
