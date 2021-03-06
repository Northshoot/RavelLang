package ai.harmony.ravel.compiler.types;

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
    StructType(String name, boolean mutable) {
        this.name = name;
        this.mutable = mutable;
    }

    StructType constructImmutable() {
        return new StructType(name, false);
    }

    public StructType makeImmutable() {
        if (!mutable)
            return this;

        if (immutableVersion != null)
            return immutableVersion;

        immutableVersion = constructImmutable();
        immutableVersion.memberNames.addAll(memberNames);
        immutableVersion.memberTypes.putAll(memberTypes);
        return immutableVersion;
    }

    public void addField(String name, Type type) {
        if (memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate field " + name + " in struct " + this.name);
        memberNames.add(name);
        memberTypes.put(name, type);

        if (immutableVersion != null)
            immutableVersion.addField(name, type);
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
        return (mutable ? "" : "const ") + name;
    }

    @Override
    public boolean isAssignable(Type type) {
        if (!mutable && type instanceof StructType && this == ((StructType) type).immutableVersion) {
            return true;
        } else {
            return CompoundType.super.isAssignable(type);
        }
    }

    @Override
    public boolean equalsExceptQualifiers(Type type) {
        if (type == null)
            return false;
        if (type.getClass() != this.getClass())
            return false;

        StructType otherStruct = (StructType)type;
        if (this == otherStruct.immutableVersion || this.immutableVersion == otherStruct)
            return true;

        return this == otherStruct;
    }

    @Override
    public int getSerializedSize() {
        int size = 0;

        for (Map.Entry<String, Type> entry : memberTypes.entrySet()) {
            int entrySize = entry.getValue().getSerializedSize();
            if (entrySize < 0)
                return -1;
            size += entrySize;
        }

        return size;
    }
}
