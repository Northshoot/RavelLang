package patterns.src.java;

import org.apache.commons.lang3.ArrayUtils;
import patterns.src.java.controller.ModelController;
import patterns.src.java.model.Model;
import patterns.src.java.rrt.AppDispatcher;
import patterns.src.java.rrt.RavelPacket;
import patterns.src.java.tiers.AndroidDriver;
import patterns.src.java.tiers.Endpoint;
import patterns.src.java.utils.ByteWork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by lauril on 1/23/17.
 */
public class TestTemplates {


    public static void testRavelPacket(){

        byte[] src= ByteBuffer.allocate(4).putInt(30).array();
        byte[] dst= ByteBuffer.allocate(4).putInt(22222).array();
        byte[] partial= ByteBuffer.allocate(1).put((byte)0).array();
        byte[] model_id = ByteBuffer.allocate(1).put((byte)5).array();
        byte[] record = ByteBuffer.allocate(20).putInt(1234567890).array();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(src);
            outputStream.write(dst);
            outputStream.write(partial);
            //outputStream.write(last);
            outputStream.write(model_id);
            outputStream.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }


        RavelPacket rp = new RavelPacket(outputStream.toByteArray());
        System.out.println(rp);
    }

    public static byte[] getTestPacket(){

        byte[] src= ByteBuffer.allocate(4).putInt(30).array();
        byte[] dst= ByteBuffer.allocate(4).putInt(22222).array();
        byte[] partial= ByteBuffer.allocate(1).put((byte)0).array();
        byte[] model_id = ByteBuffer.allocate(1).put((byte)1).array();
        byte[] record = ByteBuffer.allocate(20).putInt(1234567890).array();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(src);
            outputStream.write(dst);
            outputStream.write(partial);
            outputStream.write(model_id);
            outputStream.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return outputStream.toByteArray();
    }


    public static void main(String [] args)
    {
        //AppDispather
        AppDispatcher appD = new AppDispatcher();
        //create model

        //create endpoint
        Endpoint ep0 = new Endpoint("Embedded", Endpoint.TYPE.SOCKET);
        Endpoint ep1 = new Endpoint("Cloud", Endpoint.TYPE.SOCKET);
        //create driver
        AndroidDriver androidDriver = new AndroidDriver(appD);

        androidDriver.register_endpoint(ep0);
        androidDriver.register_endpoint(ep1);
        //test diver
        androidDriver.rx_data_from_socket(getTestPacket());
        //test model API

        //test controller API

        //testRavelPacket();
    }
}
