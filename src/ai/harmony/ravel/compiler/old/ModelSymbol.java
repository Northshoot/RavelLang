package ai.harmony.ravel.compiler.old;

import ai.harmony.ravel.primitives.Field;
import ai.harmony.ravel.primitives.Model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/18/16.
 */
public class ModelSymbol extends Symbol implements Scope {


    Map<String, Symbol> blocks = new LinkedHashMap<>();
    Scope enclosingScope;
    Model mModel;
    String mName ;


    public void makeObjects(){
            SchemaSymbol schema = (SchemaSymbol)blocks.get("schema");
            Map<String, Symbol> fieldSym = schema.getChildSymbols();
            Iterator it = fieldSym.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                FieldSymbol fs = (FieldSymbol) pair.getValue();
                mModel.addField(fs.name , fieldFromSymbol(fs));
                it.remove(); // avoids a ConcurrentModificationException
        }

    }

    public Field fieldFromSymbol(FieldSymbol fs){
        return new Field(fs.getName(), fs.mTypeName, mModel, Field.getType(fs.type));
    }
    public ModelSymbol(String name, Symbol.Type mType, String type, Scope currentScope) {
        super(name, mType);
        this.mName = name;
        mModel = new Model(mName, Model.getType(type));
        this.enclosingScope = currentScope;


    }

    public Model  getModel(){
        return mModel;
    }
    public Symbol resolve(String name) {
        Symbol s = blocks.get(name);
        if ( s!=null ) return s;
        // if not here, check any enclosing scope
        if ( getEnclosingScope() != null ) {
            return getEnclosingScope().resolve(name);
        }
        return null; // not found
    }

    public void define(Symbol sym) {
        blocks.put(sym.name, sym);

        sym.scope = this; // track the scope in each symbol
    }

    public Scope getEnclosingScope() { return enclosingScope; }
    public String getScopeName() { return name; }

    public String toString() { return "Model"+super.toString()+":"+blocks.values(); }


}
