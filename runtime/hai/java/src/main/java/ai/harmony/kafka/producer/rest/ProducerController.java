package ai.harmony.kafka.producer.rest;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.harmony.kafka.producer.KafkaTestProducer;

@RestController
public class ProducerController {
    private static final String template = "Hello, %s!";
    private KafkaTestProducer cp;
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/status")
    public StatusMessage status() {
        return new StatusMessage(KafkaTestProducer.getInstance().getMessageCounter(), KafkaTestProducer.getInstance().getByteCounter());
    }
    
    @RequestMapping("/settings")
    public SettingsMessage setting() {
        return new SettingsMessage(KafkaTestProducer.getInstance().getMessageSize(), KafkaTestProducer.getInstance().getMessagesPerSecond());
    }
    
    @RequestMapping("/mps")
    public SettingsMessage mps(@RequestParam(value="set") String set) {
        double dmps = Double.parseDouble(set);
        KafkaTestProducer.getInstance().setMessagePerSecond(dmps);
        return new SettingsMessage(KafkaTestProducer.getInstance().getMessageSize(), KafkaTestProducer.getInstance().getMessagesPerSecond());
    }
    
    @RequestMapping("/ms")
    public SettingsMessage ms(@RequestParam(value="set") String set) {
        int lms = Integer.parseInt(set);
        KafkaTestProducer.getInstance().setMessageSize(lms);
        return new SettingsMessage(KafkaTestProducer.getInstance().getMessageSize(), KafkaTestProducer.getInstance().getMessagesPerSecond());
    }
}
