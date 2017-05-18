package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.RavelPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * A client side TCP socket that talks to a remote
 * TcpEndpoint
 *
 * Created by gcampagn on 2/8/17.
 */
class RavelSocketClient implements RavelSocket {
    private final int identity;
    private final TcpEndpoint remote;
    private final JavaDriver driver;

    private Socket socket;
    private Thread listeningThread;

    RavelSocketClient(int identity, TcpEndpoint remote, JavaDriver driver) throws RavelIOException {
        this.identity = identity;
        this.remote = remote;
        this.driver = driver;

        try {
            connect();
        } catch(IOException e) {

            throw new RavelIOException(e);
        }
    }

    private void readLoop() {
        InputStream is;
        synchronized (this) {
            try {
                if (socket != null)
                    is = socket.getInputStream();
                else
                    return;
            } catch(IOException e) {
                System.err.println("IOException during connection to " + remote.getAddress() + ":" + remote.getPort());
                e.printStackTrace(System.err);
                closeSocket();
                return;
            }
        }
        while (true) {
            try {
                RavelPacket pkt = RavelSocketProtocol.readInput(is);
                driver.packetReceived(pkt, remote);
            } catch(EOFException e) {

                closeSocket();
                return;
            } catch(IOException e) {
                // FIXME report the error to the dispatcher

                System.err.println("IOException on client socket to " + remote.getAddress() + ":" + remote.getPort());
                e.printStackTrace(System.err);
                closeSocket();
                return;
            }
        }
    }

    private void connect()  throws IOException{
        if (socket != null)
            return;

        socket = new Socket(remote.getAddress(), remote.getPort());
        RavelSocketProtocol.writeIdentity(socket.getOutputStream(), identity);
        remote.connected();
        try {
            listeningThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    readLoop();
                }
            });
            listeningThread.start();
        } catch ( Exception e){
            System.err.println("Disconect");
            throw new IOException(e);
        }
    }

    private synchronized void closeSocket() {
        try {

            socket.close();
            remote.disconnected();
            socket = null;
        } catch (IOException e) {
            System.err.println("Failed to close client socket to " + remote.getAddress() + ":" + remote.getPort());
            e.printStackTrace(System.err);
        }
    }

    private void closeSocketAndJoin() {
        closeSocket();
        try {
            System.err.println("Closing socket  and joining");
            listeningThread.join();
        } catch(InterruptedException e) {
            System.err.println("Interrupted while terminating listening thread");
        }
    }

    @Override
    public synchronized void write(RavelPacket pkt) throws RavelIOException {
        try {
            connect();
            try {
                RavelSocketProtocol.writeOutput(socket.getOutputStream(), pkt);
            } finally {
                if (socket.isClosed() || socket.isInputShutdown()) {
                    closeSocketAndJoin();
                }
            }
        } catch(IOException e) {
            throw new RavelIOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        closeSocketAndJoin();
    }
}