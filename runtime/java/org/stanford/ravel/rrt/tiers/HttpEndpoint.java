package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/31/17.
 */
public class HttpEndpoint extends Endpoint{
//    interface CloudEndpoint:
//        configuration:
//        base: '171.64.70.90/'
//        port: 4444
//        type: HTTP
//        method: POST
//        url: <app.name>/




    public HttpEndpoint(){
        super();
        super.name = "";
        super.port = 4444;
        super.base = "171.64.70.90/";
        super.method = "POST";
        super.url = "simple/";
        super.type = TYPE.HTTP;
        super.USER_AGENT = "Ravel:Java/Client";
    }
}
