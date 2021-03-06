package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.utils.ByteWork;
import org.stanford.ravel.rrt.tiers.RavelIdentity;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The ravel protocol, over TCP/IP sockets
 *
 * Created by lauril on 1/27/17.
 */
public class RavelSocketProtocol {
    public static final int DEFAULT_PORT = 2222;

    private RavelSocketProtocol() {
    }

    private static void readLoop(InputStream in, byte[] into, int off) throws IOException {
        do {
            int count = in.read(into, off, into.length - off);
            if (count < 0)
                throw new EOFException("Connection terminated early");
            off += count;
        } while (off < into.length);
    }

    public static RavelPacket readInput(InputStream in) throws IOException {
        // read two bytes to figure out the length of this packet, and at the same time
        // read as much ravel packet as possible
        byte[] small = new byte[2 + RavelPacket.MIN_LENGTH];
        readLoop(in, small, 0);

        int length = ByteWork.convertTwoUnsignedBytesToInt(new byte[]{ small[0], small[1] });
        //System.out.println("Length: " + length);
        byte[] full = new byte[length];
        System.arraycopy(small, 2, full, 0, RavelPacket.MIN_LENGTH);

        readLoop(in, full, RavelPacket.MIN_LENGTH);
        return RavelPacket.fromNetwork(full);
    }

    public static void writeOutput(OutputStream out, RavelPacket pkt) throws IOException {
        byte[] bytes = pkt.toBytes();
        int length = bytes.length;
        synchronized (out) {
            out.write(ByteWork.getLengthByteArray(length));
            out.write(bytes);
        }
    }

//    public static void writeIdentity(OutputStream out, int identity) throws IOException {
//        out.write(ByteWork.getByteArray(identity));
//    }
    public static void writeIdentity(OutputStream out, RavelIdentity identity) throws IOException {
        out.write(RavelIdentity.identityToBytes(identity));
    }

//    public static int readIdentity(InputStream in) throws IOException {
//        byte[] buf = new byte[4];
//        readLoop(in, buf, 0);
//        return ByteWork.convertFourBytesToInt(buf);
//    }

    public static RavelIdentity readIdentity(InputStream in) throws IOException, ClassNotFoundException {
        byte[] buf = new byte[RavelIdentity.length];
        readLoop(in, buf, 0);
        return RavelIdentity.identityFromBytes(buf);
    }
}
