package ai.harmony.ravel.compiler.symbol;

import ai.harmony.antlr4.RavelParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lauril on 8/25/16.
 */
public class ControllerSymbol extends ComponentSymbol {

    public ControllerSymbol(String name) {
        super(name);
        super.setTypeIndex(RavelParser.CONTROLLER);
    }

    public List<EventSymbol> getEvents(){
        return getSymbols().stream()
                .filter(s -> s instanceof EventSymbol)
                .map(s -> (EventSymbol) s)
                .collect(Collectors.toList());
    }

    public void define(Symbol sym) throws IllegalArgumentException {
        if( sym instanceof EventSymbol) {
            defineEvent((EventSymbol) sym);
        } else {
            super.define(sym);
        }

    }

    private void defineEvent(EventSymbol esym) throws IllegalArgumentException{
        //the event must be connected
        String[] objEvent = esym.getName().split("\\.");
        //deal if object if object event is accessed directly
        if(objEvent.length <2 ){
            String declared = "";
            String dotted = "Error: can not access object events directly, please specify the doted path";
            //is event declared at all?
            if( ! symbols.containsKey(esym.getName()))
                declared = "Error: event " +  esym.getName() + " object is not declared\n";
            int line_err = esym.getDefNode().start.getLine();
            throw new IllegalArgumentException("line: " + line_err + " " + declared + dotted);
        }
        if ( ! symbols.containsKey(objEvent[0]) ) {
            int line_err = esym.getDefNode().start.getLine();
            throw new IllegalArgumentException("line: " + line_err + " cant find event object " + objEvent[0] + " declaration for event "+esym.getName());
        }
        esym.setScope(this);
        esym.setInsertionOrderNumber(symbols.size()); // set to insertion position from 0
        symbols.put(esym.getName(), esym);

    }
}