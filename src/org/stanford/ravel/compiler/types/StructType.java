package org.stanford.ravel.compiler.types;

import java.util.*;

/**
 * A user-defined compound type.
 *
 * Created by gcampagn on 1/20/17.
 */
public class StructType implements CompoundType {
    private final String name;
    private final List<String> memberNames = new ArrayList<>();
    private final HashMap<String, Type> memberTypes = new HashMap<>();
    private final boolean mutable;

    private StructType immutableVersion;

    public StructType(String name) {
        this(name, true);
    }
    private StructType(String name, boolean mutable) {
        this.name = name;
        this.mutable = mutable;
    }

    public StructType makeImmutable() {
        if (!mutable)
            return this;

        if (immutableVersion != null)
            return immutableVersion;

        immutableVersion = new StructType(name, false);
        immutableVersion.memberNames.addAll(memberNames);
        immutableVersion.memberTypes.putAll(memberTypes);
        return immutableVersion;
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
    public boolean isWritable(String member) {
        return mutable;
    }

    @Override
    public String getName() {
        return name;
    }
}
