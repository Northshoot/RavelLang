package edu.stanford.ravel.primitives;

import edu.stanford.ravel.compiler.symbol.ViewSymbol;
import edu.stanford.ravel.compiler.symbol.MethodDeclarationSymbol;
import edu.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Created by lauril on 8/18/16.
 */
public class View extends ConfigurableComponent {
    private final ViewSymbol symbol;
    private final List<MethodDeclarationSymbol> events = new ArrayList<>();
    private final List<MethodDeclarationSymbol> functions = new ArrayList<>();
    private final Map<String, Model> dependentModels = new HashMap<>();

    public View(String name, ViewSymbol symbol) {
        super(name);
        this.symbol = symbol;
    }

    public ConcreteView instantiate(Space space) {
        return new ConcreteView(space, this);
    }

    public Type getType() {
        return symbol.getDefinedType();
    }

    public List<MethodDeclarationSymbol> getEvents() {
        return Collections.unmodifiableList(events);
    }
    public void addEvent(MethodDeclarationSymbol event) {
        events.add(event);
    }

    public List<MethodDeclarationSymbol> getFunctions() {
        return Collections.unmodifiableList(functions);
    }
    public void addFunction(MethodDeclarationSymbol function) {
        functions.add(function);
    }

    public void dependOnModel(String alias, Model model) {
        assert model != null;
        dependentModels.put(alias, model);
    }
    public Map<String, Model> getModels() {
        return Collections.unmodifiableMap(dependentModels);
    }
}
