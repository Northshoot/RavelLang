package org.stanford.ravel.compiler.types;

import java.util.*;

/**
 * Created by gcampagn on 1/20/17.
 */
public class StructType implements CompoundType {
    private final String name;
    private final List<String> memberNames = new ArrayList<>();
    private final HashMap<String, Type> memberTypes = new HashMap<>();

    public StructType(String name) {
        this.name = name;
    }

    public void addField(String name, Type type) {
        if (memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate field " + name + " in struct " + this.name);
        memberNames.add(name);
        memberTypes.put(name, type);
    }

    @Override
    public Collection<String> getMemberList() {
        return memberNames;
    }

    @Override
    public Type getMemberType(String member) {
        return memberTypes.get(member);
    }

    @Override
    public String getName() {
        return name;
    }
}
