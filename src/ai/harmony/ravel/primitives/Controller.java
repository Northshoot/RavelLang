package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lauril on 7/21/16.
 */
public class Controller extends Primitive{
    RavelParser.ControllerDeclarationContext ctx;
    private List<Configuration> mConfigurations;
    private List<Event> mEvents;

    public Controller(RavelParser.ControllerDeclarationContext ctx){
        ctx = ctx;
        mConfigurations = new LinkedList<>();
        //create configurations from context
        mEvents = new LinkedList<>();
        //create events from context
    }

}
