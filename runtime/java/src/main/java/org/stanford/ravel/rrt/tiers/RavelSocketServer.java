package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A socket server that accepts connections from multiple remote
 * clients.
 *
 * Created by lauril on 1/27/17.
 */
public class RavelSocketServer implements Runnable, Closeable {
    private class ClientSocket implements RavelSocket {
        private final Socket socket;
        private final TcpEndpoint endpoint;
        private final Thread listeningThread;

        public ClientSocket(Socket socket) throws IOException {
            this.socket = socket;

            int identity = RavelSocketProtocol.readIdentity(socket.getInputStream());
            this.endpoint = new TcpEndpoint(identity, (InetSocketAddress)socket.getRemoteSocketAddress());
            endpoint.connected();
            listeningThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    readLoop(ClientSocket.this);
                }
            });
            listeningThread.setName("ClientSocket " + endpoint.getAddress() + ":" + endpoint.getPort());
            listeningThread.start();
        }

        public TcpEndpoint getEndpoint() {
            return endpoint;
        }

        public RavelPacket read() throws IOException {
            return RavelSocketProtocol.readInput(socket.getInputStream());
        }

        InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public void write(RavelPacket pkt) throws RavelIOException {
            try {
                RavelSocketProtocol.writeOutput(socket.getOutputStream(), pkt);
            } catch (IOException e) {
                throw new RavelIOException(e);
            }
        }

        @Override
        public void close() throws IOException {
            socket.close();
            endpoint.disconnected();
            try {
                listeningThread.join();
            } catch(InterruptedException e) {
                System.err.println("Interrupted while terminating listening thread");
            }
        }
    }

    private final ServerSocket listeningSocket;
    private volatile boolean closed = false;
    private final Thread listeningThread;
    private final JavaDriver driver;
    private final DispatcherAPI dispatcher;

    public RavelSocketServer(TcpEndpoint endpoint, JavaDriver driver, DispatcherAPI dispatcher) throws RavelIOException {
        this.driver = driver;
        this.dispatcher = dispatcher;

        try {
            listeningSocket = new ServerSocket();
            listeningSocket.bind(new InetSocketAddress(endpoint.getAddress(), endpoint.getPort()));
        } catch(IOException e) {
            throw new RavelIOException(e);
        }
        listeningThread = new Thread(this);
        listeningThread.setName("SocketServer " + endpoint.getAddress() + ":" + endpoint.getPort());
        listeningThread.start();
    }

    private void closeClient(ClientSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            pprint("Failed to close client socket to " + socket.getEndpoint());
            e.printStackTrace(System.err);
        }
        driver.removeSocket(socket.getEndpoint());
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void readLoop(ClientSocket socket) {
        try {
            InputStream is = socket.getInputStream();

            while (true) {
                RavelPacket pkt = RavelSocketProtocol.readInput(is);
                dispatcher.driver__dataReceived(pkt, socket.getEndpoint());
            }
        } catch (EOFException e) {
            closeClient(socket);
        } catch (IOException e) {
            // FIXME report the error to the dispatcher
            pprint("IOException on client socket to " + socket.getEndpoint());
            e.printStackTrace(System.err);
            closeClient(socket);
        }
    }

    @Override
    public void run() {
        pprint("Server running");
        while(true) {
            try {
                Socket socket = listeningSocket.accept();

                try {
                    ClientSocket client = new ClientSocket(socket);
                    pprint("New connection from " + client.getEndpoint());
                    driver.registerSocket(client.getEndpoint(), client);
                } catch(IOException e) {
                    pprint("IOException while preparing client socket");
                    e.printStackTrace(System.err);
                    try {
                        socket.close();
                    } catch(IOException e2) {
                        // ignore errors when closing
                    }
                }
            } catch(IOException e) {
                if (!closed)
                    e.printStackTrace();
                break;
            }
        }
    }

    public void close() throws IOException {
        closed = true;
        listeningSocket.close();
        try {
            listeningThread.join();
        } catch(InterruptedException e) {
            pprint("Interrupted while terminating listening thread");
        }
    }

    private void pprint(String s){
        System.err.println("[RavelSocketServer::]>" + s);
    }
}
