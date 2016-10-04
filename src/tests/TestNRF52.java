package tests;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.nrf52.nrf52Platform;
import ai.harmony.ravel.primitives.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 */
public class TestNRF52 {

    public static void main(String... args) throws IOException {
        String mBuildPath = null;
        mBuildPath = new File("").getAbsolutePath();
        mBuildPath+="/src/tests/src/";
        System.out.println("Build path " + mBuildPath);
        Controller ctr = new Controller(null);
        nrf52Platform api = new nrf52Platform(ctr, mBuildPath);
        api.addTimer("random_ctr");
        for(FileObject fo: api.getFiles()){
            System.out.println("Writing file: " + fo.getFileName() + " to "  + fo.getPath());
            fo.toFile();


        }
    }
}
