package patterns.src.java.rrt;

import patterns.src.java.tiers.AndroidDriver;

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


    public String processInput(InputStream in, InetAddress inetAddress) {
        //TODO: fix the hack
        byte[] bytes = new byte[16*1024];
        try {
            int count;
            while ((count = in.read(bytes)) > 0) {
            }
            this.androidDriver.rx_data_from_socket(bytes, count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "OK";
    }
}
