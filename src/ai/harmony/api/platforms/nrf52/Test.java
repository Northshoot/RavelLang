package ai.harmony.api.platforms.nrf52;

import ai.harmony.ravel.primitives.Controller;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lauril on 9/21/16.
 */
public class Test {

    public static void main(String... args) throws IOException {
        Controller ctr = new Controller(null);
        nrf52Platform api = new nrf52Platform(ctr);
        api.addTimer("random_ctr");
        Map<String, Object> mf = api.getFiles();
        for(String key : mf.keySet()){
            System.out.println("Printing for " + key);
            System.out.println(mf.get(key));

        }
    }
}
