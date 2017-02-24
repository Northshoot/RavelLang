package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/31/17.
 */

import org.stanford.ravel.rrt.RavelPacket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpClient implements RavelSocket {
        private HttpEndpoint endpoint; //contains base information

        public HttpClient (Endpoint endpoint) {
            this.endpoint = (HttpEndpoint) endpoint;
        }

        @Override
        public void write(RavelPacket data) throws RavelIOException {
            try {
                int responseCode;
                switch (this.endpoint.getMethod()) {
                    case "GET":
                    case "HEAD":
                        responseCode = get(data.toBytes());
                        break;
                    default:
                        responseCode = post(data.toBytes());
                        break;
                }

                if (responseCode != 200)
                    throw new RavelIOException(Error.SYSTEM_ERROR);
            } catch(IOException e) {
                throw new RavelIOException(e);
            }
        }

        // HTTP GET request
        public int get(byte[] data) throws IOException {

            URL obj = endpoint.getUrl();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(endpoint.getMethod());
            //add request header
            con.setRequestProperty("User-Agent", endpoint.getUserAgent());

            int responseCode = con.getResponseCode();
            //TODO: handle errors
            System.out.println("\nSending 'GET' request to URL : " + obj.toString());
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return responseCode;
        }

        // HTTP POST request
        public int post(byte[] data) throws IOException {

            URL obj = endpoint.getUrl();;
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(endpoint.getMethod());
            con.setRequestProperty("User-Agent", endpoint.getUserAgent());

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //TODO: handle errors
            System.out.println("\nSending 'POST' request to URL : " + obj.toString());
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return responseCode;
        }

    @Override
    public void close() throws IOException {

    }
}
