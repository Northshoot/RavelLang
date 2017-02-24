package patterns.src.java;

import org.stanford.ravel.rrt.RavelPacket;
import patterns.src.java.app.AppDispatcher;

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


        RavelPacket rp = RavelPacket.empty(30, 5, 1);
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
        Thread embedded_thread = new Thread(() -> {
            AppDispatcher embedded = new AppDispatcher("Embedded");
        });
        Thread gateway_thread = new Thread(() -> {
            AppDispatcher gateway = new AppDispatcher("Gateway");
        });

        // Do not create a cloud thread
        // The cloud thread wants to listen on HTTP, which it cannot do
        /*Thread cloud_thread = new Thread(() -> {
            AppDispatcher cloud = new AppDispatcher("Cloud");
        });*/

        gateway_thread.start();
        embedded_thread.start();
        //cloud_thread.start();

        //test diver
       // androidDriver.rx_data_from_socket(getTestPacket());
        //test model API

        //test controller API

        //testRavelPacket();

    }
}
