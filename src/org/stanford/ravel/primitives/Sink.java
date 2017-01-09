package org.stanford.ravel.primitives;

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
}
