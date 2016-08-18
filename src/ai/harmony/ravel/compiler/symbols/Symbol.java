package ai.harmony.ravel.compiler.symbols;

/**
 * Created by lauril on 8/18/16.
 */


import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scopes.Scope;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
 ***/
public class Symbol { // A generic programming language symbol

    public  enum Type {
                    tINVALID,
                     /** basic types */
                    T_INTEGER, T_NUMBER,
                    /** fields */
                    T_INTEGER_FIED, T_NUMBER_FIED, T_DATE_FIED,
                    T_DATE_TIME_FIED, T_TIME_STAMP_FIELD,
                    /** components */
                    MODEL, CONTROLLER, VIEW, FLOW

    }

    public String name;
    public Type type;
    public Scope scope;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this(name);
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        if ( type!=Type.tINVALID ) return '<'+getName()+":"+type+'>';
        return getName();
    }


    public static Symbol.Type getType(int tokenType) {
        switch ( tokenType ) {
            case RavelParser.T_DATE_FIED :  return Type.T_DATE_FIED;
            case RavelParser.T_DATE_TIME_FIED :   return Type.T_DATE_TIME_FIED;
            case RavelParser.T_INTEGER :   return Type.T_INTEGER;
            case RavelParser.T_INTEGER_FIED :   return Type.T_INTEGER_FIED;
            case RavelParser.T_NUMBER :   return Type.T_NUMBER;
            case RavelParser.T_NUMBER_FIED :   return Type.T_NUMBER_FIED;
            case RavelParser.T_TIME_STAMP_FIELD :   return Type.T_TIME_STAMP_FIELD;
            case RavelParser.MODEL :   return Type.MODEL;
            case RavelParser.CONTROLLER :   return Type.CONTROLLER;
            case RavelParser.FLOW :   return Type.FLOW;
            case RavelParser.VIEW :   return Type.VIEW;
            //case RavelParser :   return Type;
            //case RavelParser :   return Type;
        }
        return Symbol.Type.tINVALID;
    }

}

