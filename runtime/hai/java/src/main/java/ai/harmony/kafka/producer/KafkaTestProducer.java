package ai.harmony.kafka.producer;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
//import util.properties packages
import java.util.Properties;

//import simple producer packages
import org.apache.kafka.clients.producer.Producer;
import org.apache.commons.lang3.StringUtils;
//import KafkaProducer packages
import org.apache.kafka.clients.producer.KafkaProducer;

//import ProducerRecord packages
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.harmony.kafka.producer.connector.ConsumerStatusMessage;
import ai.harmony.kafka.producer.util.CLParameters;

public class KafkaTestProducer implements Observer {
    private final Logger LOG = LoggerFactory.getLogger(KafkaTestProducer.class);
    private static KafkaTestProducer cp = null;
    
    private String bootstrap;
    private String topic;
    private LinkedList<WorkerTask> workers;
    private boolean shutdown;
    private int messageSize;
    private double messagesPerSecond;
    private double calculatedMessagesPerSecond;
    private int numProducers;
    private CLParameters params;
    private long updateTimeout;

    private KafkaTestProducer(CLParameters params) {
        bootstrap = params.bootstrap;
        topic = params.topic;     
        shutdown = false;
        workers = new LinkedList<WorkerTask>();
        messageSize = 1;
        messagesPerSecond = 1000; // A sain default
        calculatedMessagesPerSecond = messagesPerSecond;
        updateTimeout = System.currentTimeMillis() + 1000*10;
        numProducers = params.producers;
        this.params = params;
        for (int i = 0; i < numProducers; ++i) {
            WorkerTask w = new WorkerTask();
            Thread workerThread = new Thread(w);
            workers.add(w);
            workerThread.start();
        }
    }
    
    public void setMessageSize(int size) {
        LOG.info(String.format("Set ms %d", size));
        messageSize = size;
        updateTimeout = System.currentTimeMillis() + 1000*10;
        for (WorkerTask wt : workers) {
            if (messageSize == 0) {
                wt.messageCounter = 0;
                wt.byteCounter = 0;
            }
            synchronized(wt.wait) {
                wt.wait.notify();
            }
        }
    }
    
    public void setMessagePerSecond(double mps) {
        LOG.info(String.format("Set mps %f", mps));
        messagesPerSecond = mps;
        calculatedMessagesPerSecond = mps;
        updateTimeout = System.currentTimeMillis() + 1000*10;
    }
    
    public long getMessageSize() {
        return messageSize;
    }

    public double getMessagesPerSecond() {
        return messagesPerSecond;
    }
    
    public void shutdown() {
        shutdown = true;
    }
    
    public long getByteCounter() {
        long byteCounter = 0;
        for (WorkerTask wt : workers) {
            byteCounter += wt.byteCounter;
        }
        return byteCounter;
    }

    public long getMessageCounter() { 
        long messageCounter = 0;
        for (WorkerTask wt : workers) {
            messageCounter += wt.messageCounter;
        }
        return messageCounter;
    }
    
    private String createMessage(int size) {
        String stringToPad = String.valueOf(System.nanoTime()) + " ";
        return StringUtils.rightPad(stringToPad, size, '*');
    }
    
    private long setupSleep(double mps) {
        if (mps == 0) {
            return 1;
        } else {
            return (long)( 1000000000.0 / mps ) * numProducers;
        }
    }

    private Producer<String, String> createProducer(String bootstrapServers) {
        // create instance for properties to access producer configs   
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", bootstrapServers);

        //Set acknowledgements for producer requests.      
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", params.batch);

        //Reduce the no of requests less than 0   
        props.put("linger.ms", params.linger);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.   
        props.put("buffer.memory", params.buffer);
        
        props.put("compression.type", "snappy");

        props.put("key.serializer", 
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", 
                "org.apache.kafka.common.serialization.StringSerializer");

        return new KafkaProducer<String, String>(props);
    }
    
    private class WorkerTask implements Runnable {
        public Object wait = new Object();
        public long messageCounter;
        public long byteCounter;
        private Producer<String, String> producer;
        
        public void run() {
            producer = createProducer(bootstrap);
            while(!shutdown) {
                try {
                    long startTime = System.nanoTime();
                    long sleepNanos = setupSleep(calculatedMessagesPerSecond);
                    if (messageSize != 0) {
                        String msg = createMessage(messageSize);
                        producer.send(new ProducerRecord<String, String>(topic, null, msg));
                        messageCounter++;
                        byteCounter += msg.length();
                        LOG.debug(String.format("Published message %d", messageCounter));
                        
                        while (sleepNanos > (System.nanoTime() - startTime));
//                        while (sl)
//                        if (next > 0) {
//                            LOG.debug(String.format("Sleep %d ms %d ns", next/1000000, (int)(next % 1000000)));
//                            Thread.sleep(next/1000000, (int)(next % 1000000));
//                        }
                    } else {
                        synchronized (wait) {
                            LOG.debug("Wait for messagesize != 0");
                            wait.wait(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            producer.close();
        }
    }

    public synchronized static KafkaTestProducer getInstance() {
        if (cp == null) {
            cp = new KafkaTestProducer(CLParameters.get());
        }
        return cp;
    }

    public void update(Observable o, Object arg) {
        double calculated;
        if (updateTimeout > System.currentTimeMillis()) {
            return;
        }
        if (!(arg instanceof ConsumerStatusMessage) || messageSize == 0 || messagesPerSecond == 0) {
            return; 
        }
        ConsumerStatusMessage msg = (ConsumerStatusMessage) arg;
        
        if ((msg.getLatency()/1000000) > 1000) {
            // If latency gets to high we assume that the consumer cant handle the incoming packages.
            LOG.info("Latency is to big");
            calculatedMessagesPerSecond = messagesPerSecond;
            return;
        }
        
        calculated = calculatedMessagesPerSecond + (messagesPerSecond - msg.getThroughputMessages());
        if (calculated < 0) {
            calculatedMessagesPerSecond = messagesPerSecond;
        } else {
            calculatedMessagesPerSecond = calculated;
        }
        
        LOG.info("New mps {}", calculatedMessagesPerSecond);
    }
}

