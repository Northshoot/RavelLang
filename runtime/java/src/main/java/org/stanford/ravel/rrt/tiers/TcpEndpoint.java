package org.stanford.ravel.rrt.tiers;

import java.net.InetSocketAddress;
import java.net.URI;

import static org.stanford.ravel.rrt.tiers.RavelSocketProtocol.DEFAULT_PORT;

/**
 * A TCP endpoint, as seen from the client
 *
 * Created by gcampagn on 2/8/17.
 */
public class TcpEndpoint extends Endpoint {
    private final int port;
    private final String address;

    TcpEndpoint(int id, URI url) {
        super(TYPE.SOCKET, id);

        int port = url.getPort();
        if (port < 0)
            port = DEFAULT_PORT;
        this.port = port;
        address = url.getHost();
    }

    TcpEndpoint(int id, InetSocketAddress address) {
        super(TYPE.SOCKET, id);

        this.port = address.getPort();
        this.address = address.getHostString();
    }

    String getAddress() {
        return address;
    }

    int getPort() {
        return port;
    }
}
