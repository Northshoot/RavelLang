package ai.harmony.kafka.producer.rest;

public class SettingsMessage {
    private long messageSize;
    private double messagesPerSecond;
    public SettingsMessage(long messageSize, double messagesPerSecond) {
        this.messageSize = messageSize;
        this.messagesPerSecond = messagesPerSecond;
    }
    
    public long getMessageSize() { return messageSize; }
    public double getMessagesPerSecond() { return messagesPerSecond; }
}
