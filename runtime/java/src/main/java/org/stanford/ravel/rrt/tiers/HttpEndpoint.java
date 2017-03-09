package org.stanford.ravel.rrt.tiers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by lauril on 1/31/17.
 */
public class HttpEndpoint extends Endpoint {
    private final URL url;
    private final String method;
    private final String user_agent;

    HttpEndpoint(String name, URI url) throws MalformedURLException {
        this(name, url, null, null);
    }

    HttpEndpoint(String name, URI url, String method, String user_agent) throws MalformedURLException {
        super(TYPE.HTTP, name);
        this.url = url.toURL();
        this.method = method != null ? method : "POST";
        this.user_agent = user_agent != null ? user_agent : "Ravel-Java-Client/1.0";
    }

    public String getMethod() { return method; }
    public String getUserAgent() { return user_agent; }

    URL getUrl() {
        return url;
    }
}