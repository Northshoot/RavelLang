package org.stanford.api.builder;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.primitives.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 10/6/16.
 */
public class PlatformBuilder {
    private static PlatformBuilder ourInstance = new PlatformBuilder();

    public static PlatformBuilder getInstance() {
        return ourInstance;
    }

    private RavelApplication rApp;
    private String path;
    List<FileObject> mFiles; //collect all the files to generate
    private PlatformBuilder() {
        mFiles = new ArrayList<>();
    }


    public void setApp(RavelApplication r){
        ourInstance.rApp = r;
    }

    public void buildAll(){
        for(Space s : rApp.getSpaces()){
            buildSpace(s);
        }
    }

    public void buildSpace(Space s){
        System.out.println("Building Space: \n" + s.getName());
        s.setBuildPath(path);
        mFiles.addAll(s.buildAll());
    }

    public void buildSpace(String name){
        buildSpace(rApp.getSpace(name));

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void render() {
        for (FileObject fo : mFiles) {
            fo.toFile();
        }
    }
}
