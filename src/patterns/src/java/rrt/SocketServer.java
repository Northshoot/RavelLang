package patterns.src.java.rrt;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lauril on 1/27/17.
 */
public class SocketServer extends Thread {

    private int port;
    ServerSocket serverSocket;
    Socket clientSocket;
    InputStream in;
    RavelSocketProtocol rsp;

    public SocketServer(int port, RavelSocketProtocol rsp){
        this.port = port;
        this.rsp = rsp;

    }

    public void run(){
        System.out.println("Server running");
        try {
            serverSocket = new ServerSocket(this.port);
            clientSocket = serverSocket.accept();
            System.out.println("client connected");
        } catch (IOException ex) {
            System.out.println("Can't accept client connection. ");
            ex.printStackTrace();
        }
        try {

            in =  clientSocket.getInputStream();

            // Initiate conversation with client

            rsp.processInput(in, clientSocket.getInetAddress());
        } catch (Exception e) {
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
