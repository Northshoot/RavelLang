package patterns.src.java.rrt;

import org.stanford.ravel.rrt.tiers.AndroidDriver;
import org.stanford.ravel.rrt.tiers.JavaDriver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by lauril on 1/27/17.
 */
public class RavelSocketProtocol {

    public RavelSocketProtocol(JavaDriver javaDriver){


    }

    AndroidDriver androidDriver;
    public RavelSocketProtocol(AndroidDriver androidDriver) {
        this.androidDriver = androidDriver;
    }


    public String processInput(DataInputStream in, InetAddress inetAddress) {
        //TODO: fix the hack
        //TODO:  needs more dynamic
        byte[] bytes = new byte[4*1024];
        try {
            int count = in.read(bytes);
            this.androidDriver.rx_data_from_socket(bytes, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
