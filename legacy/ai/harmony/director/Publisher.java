package ai.harmony.director;

import ai.harmony.ravel.RavelProperties;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;

import javax.json.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Publisher {

        private String domain="http://localhost:8000/";
        private String urlpath="director/register/";
        private final String user_agent ="Ravel-Java-Client/1.0";

        public Publisher () {

        }

        // HTTP POST request
        public String post(JsonObject payload) throws IOException {
            StringBuffer jsonString = new StringBuffer();
            String content = "";
            try {
                HttpClientContext context = HttpClientContext.create();
                CookieStore cookieStore = new BasicCookieStore();
                context.setCookieStore(cookieStore);

                HttpGet get = new HttpGet(this.domain);
                CloseableHttpClient httpClient = HttpClients.createDefault();

                CloseableHttpResponse response = httpClient.execute(get, context);

                System.out.println("Get response: " + response.getStatusLine().getStatusCode() + " token: " + getcsrftoken(cookieStore));

                HttpPost post = new HttpPost(this.domain + this.urlpath);
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", RavelProperties.getInstance().getUserName()));
                params.add(new BasicNameValuePair("password", RavelProperties.getInstance().getPassword()));
                params.add(new BasicNameValuePair("data", payload.toString()));
                UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
                post.setEntity(paramEntity);
                post.setHeader("User-Agent",this.user_agent);
                post.setHeader("x-csrftoken", getcsrftoken(cookieStore));

                RequestAddCookies addCookies = new RequestAddCookies();
                addCookies.process(post, context);
                response = httpClient.execute(post);
                int ok_code = response.getStatusLine().getStatusCode();
                if(ok_code == 200 || ok_code == 203){
                    jsonString.append(readResponse(response));
                } else {
                    throw new RuntimeException("Server error: response id" + ok_code);
                }

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            return jsonString.toString();
        }

    private String getcsrftoken(CookieStore cookieStore) {
        return getCookieValue(cookieStore,"csrftoken");
    }

    private String getCookieValue(CookieStore cookieStore, String cookieName) {
        String value = null;
        for (Cookie cookie: cookieStore.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                value = cookie.getValue();
            }
        }
        return value;
    }
    public String readResponse(CloseableHttpResponse response)
            throws Exception {
        BufferedReader reader = null;
        String content = "";
        String line = null;
        HttpEntity entity = response.getEntity();

        reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        while ((line = reader.readLine()) != null) {
            content += line;
        }
// ensure response is fully consumed
        EntityUtils.consume(entity);
        return content;
    }
    }


