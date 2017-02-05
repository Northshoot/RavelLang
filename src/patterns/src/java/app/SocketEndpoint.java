package patterns.src.java.app;

import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 2/1/17.
 */
public class SocketEndpoint extends HttpEndpoint {

    public SocketEndpoint(){
        super();
        this.type = Endpoint.TYPE.SOCKET;

    }

    public SocketEndpoint(String gateway, String base, int port) {
        this();
        super.name = gateway;
        super.base = base;
        super.port = port;
    }

}
