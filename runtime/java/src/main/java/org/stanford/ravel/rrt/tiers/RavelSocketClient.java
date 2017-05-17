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
    private int mNumberOfretries = 1000;
    private int mBackOffExponent = 2;
    private int getmBackOffTime = 100;

    RavelSocketClient(int identity, TcpEndpoint remote, JavaDriver driver) throws RavelIOException {
        this.identity = identity;
        this.remote = remote;
        this.driver = driver;
        System.out.println("Connecting to: " + remote);
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
                //e.printStackTrace(System.err);
                closeSocket();
                return;
            }
        }
    }

    private void connect() throws IOException {
        if (socket != null)
            return;
        int sleepTime = getmBackOffTime;
        IOException ioe = null;
        boolean notconnected = true;
        while(notconnected) {
            try{
                socket = new Socket(remote.getAddress(), remote.getPort());
                ioe = null;
                RavelSocketProtocol.writeIdentity(socket.getOutputStream(), identity);
                remote.connected();
                notconnected = false;
                listeningThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        readLoop();
                    }
                });
                listeningThread.start();
                return;
            } catch (IOException e){
                try{
                    Thread.sleep(sleepTime);
                    mNumberOfretries--;
                    sleepTime*=mBackOffExponent;
                    ioe = e;
                    System.err.println("Retry # " + mNumberOfretries);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        if (ioe != null)
            throw new IOException(ioe);


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
