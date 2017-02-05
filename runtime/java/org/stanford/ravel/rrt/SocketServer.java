package org.stanford.ravel.rrt;

import patterns.src.java.rrt.RavelSocketProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void run(){
        pprint("Server running");
        while(true) {
            try {
                clientSocket = serverSocket.accept();

                pprint("Just connected to " + clientSocket.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                rsp.processInput(in,clientSocket.getInetAddress());

                clientSocket.close();

            }catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    void pprint(String s){
        System.out.println("[SocketServer::]>" + s);
    }

}
