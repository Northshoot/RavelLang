package patterns.src.java.rrt;

/**
 * Created by lauril on 1/26/17.
 */
public interface SystemEventAPI {

    /**
     * Event that are called by system driver, has to be implemented in
     * Event consumer
     */
    
    enum SysEvents{STARTED, STOPPED, RESTARTED, BATTERY }

    void started();
    void stopped();
    void restarted();

    enum BatteryLevel {HIGH, HIGH_MID, MID, MID_LOW, LOW, CRITICAL }
    void battery(BatteryLevel bl);





}
