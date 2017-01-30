package org.stanford.ravel.primitives;

import java.util.Map;

/**
 * Created by lauril on 8/18/16.
 */
public class Sink extends Primitive{
    private String identifier;
    private String resolve;

    public Sink(String name, String res) {

        super(name);
        identifier = name;
        resolve = res;
    }
    public void sink(String name, String resolve){
        this.identifier = name;
        this.resolve = resolve;
    }

    public String getSinkIdentifier() {return this.identifier; }
    public String getSinkReference() {return this.resolve; }

    public InstantiatedSink instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedSink instantiated = new InstantiatedSink(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
    }
}
