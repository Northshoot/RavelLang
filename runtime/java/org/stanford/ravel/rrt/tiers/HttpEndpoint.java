package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/31/17.
 */
public class HttpEndpoint extends Endpoint{
    public final static String USER_AGENT = "Ravel:Java/Client";
    public String url;


    public HttpEndpoint(String name, String url){
        super(name, TYPE.HTTP);
        this.url = url;
    }
}
