package ai.harmony.kafka.producer.connector;

public class ConsumerStatusMessage {
    private long messages;
    private long bytes;
    private long latency;
    private long throughputBytes;
    private long throughputMessages;

    public long getMessages() {
        return messages;
    }

    public long getBytes() {
        return bytes;
    }
    
    public long getLatency() {
        return latency;
    }
    
    public long getThroughputBytes() {
        return throughputBytes;
    }
    
    public long getThroughputMessages() {
        return throughputMessages;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public void setThroughputBytes(long throughputBytes) {
        this.throughputBytes = throughputBytes;
    }

    public void setThroughputMessages(long throughputMessages) {
        this.throughputMessages = throughputMessages;
    }
    
    @Override
    public String toString() {
        return "ConsumerStatusMessage [messages=" + messages + ", bytes=" + bytes + ", latency=" + latency
                + ", throughputBytes=" + throughputBytes + ", throughputMessages=" + throughputMessages + "]";
    }
}
