package ai.harmony.api.platforms.nrf52;

import ai.harmony.ravel.primitives.Controller;

import java.io.IOException;

/**
 * Created by lauril on 9/21/16.
 */
public class Test {

    public static void main(String... args) throws IOException {
        Controller ctr = new Controller(null);
        nrf52Platform api = new nrf52Platform(ctr);
    }
}
