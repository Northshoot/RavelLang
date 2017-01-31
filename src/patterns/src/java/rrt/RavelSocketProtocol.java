package patterns.src.java.rrt;

import patterns.src.java.app.AppDispatcher;
import patterns.src.java.tiers.AndroidDriver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * Created by lauril on 1/27/17.
 */
public class RavelSocketProtocol {

    public RavelSocketProtocol(){


    }

    AndroidDriver androidDriver;
    public RavelSocketProtocol(AndroidDriver androidDriver) {
        this.androidDriver = androidDriver;
    }


    public String processInput(DataInputStream in, InetAddress inetAddress) {
        //TODO: fix the hack
        //TODO:  needs more dynamic
        byte[] bytes = new byte[AppDispatcher.PACKET_SIZE];
        try {
            int count = in.read(bytes);
            this.androidDriver.rx_data_from_socket(bytes, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
