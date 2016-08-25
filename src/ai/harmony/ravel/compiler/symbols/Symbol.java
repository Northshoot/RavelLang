package ai.harmony.ravel.compiler.symbols;

/**
 * Created by lauril on 8/18/16.
 */


import ai.harmony.ravel.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scopes.Scope;
import ai.harmony.ravel.primitives.Field;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
 ***/
public class Symbol {


    public  enum Type {
                    tINVALID,
                    /** fields */
                    T_INTEGER_FIELD, T_NUMBER_FIELD, T_DATE_FIELD,
                    T_DATE_TIME_FIELD, T_TIME_STAMP_FIELD, T_BYTE_FIELD,
                    T_STRING_FIELD, T_BOOLEAN_FIELD, T_CONTEXT_FIELD,
                    /** block */
                    BLOCK,
                    /** components */
                    MODEL, CONTROLLER, VIEW, EVENT, FLOW,
                    /** space stuff */
                    INSTANTIATION, REFERENCE
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
            case RavelParser.T_BOOLEAN_FIELD :   return Type.T_BOOLEAN_FIELD;
            case RavelParser.T_BYTE_FIELD :   return Type.T_BYTE_FIELD;
            case RavelParser.T_DATE_FIELD :  return Type.T_DATE_FIELD;
            case RavelParser.T_DATE_TIME_FIELD :   return Type.T_DATE_TIME_FIELD;
            case RavelParser.T_INTEGER_FIELD :   return Type.T_INTEGER_FIELD;
            case RavelParser.T_NUMBER_FIELD :   return Type.T_NUMBER_FIELD;
            case RavelParser.T_TIME_STAMP_FIELD :   return Type.T_TIME_STAMP_FIELD;
            case RavelParser.T_STRING_FIELD :   return Type.T_STRING_FIELD;
            case RavelParser.T_CONTEXT_FIELD :   return Type.T_CONTEXT_FIELD;

            case RavelParser.MODEL :   return Type.MODEL;
            case RavelParser.CONTROLLER :   return Type.CONTROLLER;
            case RavelParser.FLOW :   return Type.FLOW;
            case RavelParser.VIEW :   return Type.VIEW;
            //case RavelParser :   return Type;
        }
        return Symbol.Type.tINVALID;
    }

}

