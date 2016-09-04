package ai.harmony.ravel.primitives;

/**
 * Returns event hooks in various languages
 * translates loops and access to the variables
 * Created by lauril on 8/16/16.
 */
public class Event extends Primitive{
    private Controller mParentController;
    public Event(String name) {
        super(name);
    }

    public void setController(Controller c){ this.mParentController = c ;}




}
