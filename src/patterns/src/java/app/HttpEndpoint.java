package patterns.src.java.app;

import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 1/31/17.
 */
public class HttpEndpoint extends Endpoint {
//    interface CloudEndpoint:
//        configuration:
//        base: '171.64.70.90/'
//        port: 4444
//        type: HTTP
//        method: POST
//        url: <app.name>/


    private String path=null;

    public HttpEndpoint(){
        super();
        super.name = "";
        super.port = 4444;
        super.base = "http://" + "171.64.70.90";
        super.method = "POST";
        super.url = "simple/";
        super.type = TYPE.HTTP;
        super.user_agent = "Ravel:Java/Client";
    }

    public HttpEndpoint(String name) {
        this();
        super.name = name;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath() { return path; }

    public String getHttpAddress(){
        String full_url = "";
        full_url += super.base;
        if(super.port >0){
            full_url += ":" + Integer.toString(super.port);
        }
        full_url +="/" + super.url;

        return full_url;
    }

    public String getFullURL(){
        if(path == null)
            return getHttpAddress();
        else
            return getHttpAddress() + this.path;
    }
}
