package ai.harmony.kafka.producer.connector;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ai.harmony.kafka.producer.util.CLParameters;

public class TestConsumer extends Observable {
    private final Logger LOG = LoggerFactory.getLogger(TestConsumer.class);
    
    private String consumerHost;
    private Timer timer;
    
    
    public TestConsumer(CLParameters clp) {
        consumerHost = clp.consumerHost;
        timer = new Timer();
        timer.schedule(new Worker(), 1000, 5000);
    }

    private class Worker extends TimerTask {
        private Client client;
        
        public Worker() {
            client = getJerseyClient();
        }
        
        public void run() {
            String url = "http://"+consumerHost+"/status";

            try {
                String jsonData;
                Response response;
                WebTarget target = client.target(url);
                Invocation.Builder invocationBuilder =
                        target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.get();

                /* Check return code */
                if (response.getStatus() != 200) {
                    LOG.info(String.format("_GET_: Wrong status code: %d", response.getStatus()));
                    response.close();
                } else {
                    LOG.debug("_GET_: Status code: " + response.getStatus());
                    
                    jsonData = response.readEntity(String.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    //convert json string to object
                    ConsumerStatusMessage msg = objectMapper.readValue(jsonData, ConsumerStatusMessage.class);
                    LOG.info(msg.toString());
                    setChanged();
                    notifyObservers(msg);
                }
            } catch (Exception pe) {
                LOG.info(String.format("_GET_: Unable to execute [ex:%s]", pe.getMessage()));
            }
        }
        
    }
    
    private static Client getJerseyClient() {
        Client client;
        // create HTTPClient and with configured connection pool

        int poolSize = 5; // connections

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(poolSize);
        //connectionManager.setDefaultMaxPerRoute(5);

        int timeout = 2000; // ms

        int readTimeout = 2000; // ms

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property( ApacheClientProperties.CONNECTION_MANAGER, connectionManager );
        clientConfig.connectorProvider( new ApacheConnectorProvider() );

        client = ClientBuilder.newClient( clientConfig );
        client.property( ClientProperties.CONNECT_TIMEOUT, timeout );
        client.property( ClientProperties.READ_TIMEOUT, readTimeout );

        /* Debugging feature for jersey client */
        /*java.util.logging.Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

                Feature feature = new LoggingFeature(logger, java.util.logging.Level.INFO, null, null);
                client.register(feature);*/
        return client;
    }
}
