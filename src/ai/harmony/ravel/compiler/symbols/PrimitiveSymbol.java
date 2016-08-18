package ai.harmony.ravel.compiler.symbols;
import ai.harmony.ravel.primitives.Type;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.compiler.scopes.ScopedSymbol;
import ai.harmony.ravel.primitives.Primitive;

import java.util.*;

/**
 * Created by lauril on 8/18/16.
 */

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
 ***/
public class PrimitiveSymbol  extends ScopedSymbol implements Scope, Type {

        /** List of all fields and methods */
        public Map<String,Symbol> members=new LinkedHashMap<>();
        public String mName;
        public String mType;
        public Scope mCurrentScope;
        public  Scope mEnclosingScope;
        public Primitive mPrimitive;



    public PrimitiveSymbol(String name, Scope enclosingScope, Scope currentScope) {
        super( name,  enclosingScope);
        this.mName = name;
        this.mType = type;
        this.mCurrentScope = currentScope;
        this.mEnclosingScope = enclosingScope;

    }




    /** For a.b, only look in a's class hierarchy to resolve b, not globals */
        public Symbol resolveMember(String name) {
            Symbol s = members.get(name);
            if ( s!=null ) return s;

            return null; // not found
        }

        public Map<String, Symbol> getMembers() { return members; }
        public String toString() {
            return "class "+name+":{"+
                    Symbol.stripBrackets(members.keySet().toString())+"}";
        }

    @Override
    public Primitive getPrimitive() {
        return null;
    }
}