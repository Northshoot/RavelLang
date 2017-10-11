package ai.harmony.kafka.producer.util;

import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import ch.qos.logback.classic.Level;


public class CLParameters {
    private static CLParameters settings;

    @Parameter(names = "--log-level", description = "Log level [OFF ERROR WARN INFO DEBUG TRACE ALL]", required = false)
    public String logLevel = "INFO";

    @Parameter(names = "--bootstrap", description = "Kafka bootstrap servers", required = true)
    public String bootstrap;
    
    @Parameter(names = "--topic", description = "Kafka topic to post to", required = true)
    public String topic;
    
    
    @Parameter(names = "--producers", description = "Number of producers threads to spawn")
    public int producers = 1;
    
    @Parameter(names = "--buffer", description = "Buffer size in bytes", required = false)
    public int buffer = 64000;
    
    @Parameter(names = "--linger", description = "Linger time in ms", required = false)
    public int linger = 1;
    
    @Parameter(names = "--batch", description = "Batch size of each producer in bytes", required = false)
    public int batch = 64000;

    @Parameter(names = "--consumer-host", description = "Address to test consumer", required = false)
    public String consumerHost = null;
    
    public static void init(String[] args) {
        CLParameters clp = new CLParameters();

        JCommander jc = new JCommander(clp);
        try {
            jc.parse(args);
        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            jc.usage();
            System.exit(1);
        }
        
        settings = clp;
        setLoglevel(settings.logLevel);
    }
    
    public static CLParameters get() {
        return settings;
    }
    
    private static void setLoglevel(String level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        if (!root.getLevel().toString().equals(level)) {
            root.setLevel(Level.toLevel(level));
        }
    }
}

