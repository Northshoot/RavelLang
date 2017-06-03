package org.stanford.ravel.rrt.tiers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author larry
 */
public abstract class HttpBaseUrlConnection {

    private String type = "application/x-www-form-urlencoded";

    protected HttpURLConnection conn = null;
    protected URL url;

    public HttpBaseUrlConnection () {

    }


    protected Response writePost(String db, String data)throws MalformedURLException,
            java.net.ConnectException,
            IOException{
        url = new URL(db);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "Content-Type", type );
        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
        output.writeBytes(data);
        output.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        conn.getInputStream()));
        String decodedString;
//        while ((decodedString = in.readLine()) != null) {
//            System.out.println(decodedString);
//        }

        return new Response(conn,in.readLine());
    }

    protected Response getData(String data)throws IOException{
        conn.setRequestMethod("GET");
        conn.setRequestProperty( "Content-Type", type );
        DataOutputStream output = new DataOutputStream(conn.getOutputStream());
        output.writeBytes(data);
        output.close();
        return new Response(conn,"");
    }

    protected class Response{
        public int responseCode;
        public String responseMessage;
        public String content;


        public Response(HttpURLConnection conn, String data) throws IOException {
            this.responseCode = conn.getResponseCode();
            this.responseMessage = conn.getResponseMessage();
            this.content = data;
        }

        public String toString(){
            return "code: " + this.responseCode + " MSG: " + this.responseMessage + " CNT: " + this.content;
        }
    }
}
