package ai.harmony.kafka.producer;

import ai.harmony.kafka.producer.connector.TestConsumer;
import ai.harmony.kafka.producer.util.CLParameters;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        CLParameters.init(args);
        KafkaTestProducer cp = KafkaTestProducer.getInstance();
        if (CLParameters.get().consumerHost != null) {
            new TestConsumer(CLParameters.get()).addObserver(cp);
        }
        SpringApplication.run(App.class, args);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        cp.shutdown();
//        System.exit(0);
    }
}
