package org.stanford.ravel.compiler.symbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.ravel.compiler.types.CompoundType;
import org.stanford.ravel.compiler.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/** A symbol representing a collection of data like a struct or class.
 *  Each member has a slot number indexed from 0 and we track data fields
 *  and methods with different slot sequences. A ComponentSymbol
 *  can also be a member of an aggregate itself (nested structs, ...).
 */
public abstract class ComponentSymbol extends SymbolWithScope implements TypeSymbol {
    protected ParserRuleContext defNode;

    //variables used to denote when concrete classes have been made for the symbols
    //
    protected boolean mInstantiated = false;
    protected boolean mHasconcreateClass = false;

    public ComponentSymbol(String name) {
        super(name);
    }

    public void setDefNode(ParserRuleContext defNode) {
        this.defNode = defNode;
    }

    public ParserRuleContext getDefNode() {
        return defNode;
    }

    @Override
    public void define(Symbol sym) throws IllegalArgumentException {
        super.define(sym);
    }

    /** Look up name within this scope only. Return any kind of MemberSymbol found
     *  or null if nothing with this name found as MemberSymbol.
     */
    public Symbol resolveMember(String name) {
        Symbol s = symbols.get(name);
        if ( s instanceof MemberSymbol ) {
            return s;
        }
        return null;
    }

    /** Look for a field with this name in this scope only.
     *  Return null if no field found.
     */
    public Symbol resolveField(String name) {
        Symbol s = resolveMember(name);
        if (s instanceof VariableSymbol) {
            return s;
        }
        return null;
    }

    /** get the number of fields defined specifically in this class */
    public int getNumberOfDefinedFields() {
        int n = 0;
        for (Symbol s : getSymbols()) {
            if (s instanceof VariableSymbol) {
                n++;
            }
        }
        return n;
    }

    /** Get the total number of fields visible to this class */
    public int getNumberOfFields() { return getNumberOfDefinedFields(); }

    /** Return the list of fields in this specific aggregate */
    public List<VariableSymbol> getDefinedFields() {
        List<VariableSymbol> fields = new ArrayList<>();
        for (Symbol s : getSymbols()) {
            if (s instanceof VariableSymbol) {
                fields.add((VariableSymbol)s);
            }
        }
        return fields;
    }

    public List<VariableSymbol> getFields() { return getDefinedFields(); }
}