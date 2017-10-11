package ai.harmony.kafka.producer.rest;

public class StatusMessage {
    private final long messages;
    private final long bytes;

    public StatusMessage(long messages, long  bytes) {
        this.bytes = bytes;
        this.messages = messages;
    }

    public long getMessages() {
        return messages;
    }

    public long getBytes() {
        return bytes;
    }
}
