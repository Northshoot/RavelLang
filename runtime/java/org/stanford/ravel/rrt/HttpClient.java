package org.stanford.ravel.rrt;

/**
 * Created by lauril on 1/31/17.
 */
import org.stanford.ravel.rrt.tiers.Endpoint;
import patterns.src.java.app.HttpEndpoint;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {


        private HttpEndpoint endpoint; //contains base information


        public HttpClient (Endpoint endpoint){
            this.endpoint = (HttpEndpoint) endpoint;
        }

        public void sendData(byte[] data) throws Exception {
            if(this.endpoint.getMethod() == "GET")
                try {
                    get(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else
                post(data);

        }
        // HTTP GET request
        public void get(byte[] data) throws Exception {

            URL obj = new URL(endpoint.getFullURL());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", endpoint.getUserAgent());

            int responseCode = con.getResponseCode();
            //TODO: handle errors
            System.out.println("\nSending 'GET' request to URL : " + endpoint.getFullURL());
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        // HTTP POST request
        public void post(byte[] data) throws Exception {

            URL obj = new URL(endpoint.getFullURL());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", endpoint.getUserAgent());


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //TODO: handle errors
            System.out.println("\nSending 'POST' request to URL : " + endpoint.getFullURL());
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }



}
