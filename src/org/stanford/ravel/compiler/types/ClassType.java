package org.stanford.ravel.compiler.types;

import java.util.*;

/**
 * The base type of all class types
 *
 * Created by gcampagn on 1/23/17.
 */
public class ClassType implements CompoundType {
    private class InstanceType implements CompoundType {
        private final List<String> fieldNames = new ArrayList<>();
        private final List<String> allMemberNames = new ArrayList<>();
        private final HashMap<String, Type> memberTypes = new HashMap<>();

        @Override
        public Collection<String> getMemberList() {
            return allMemberNames;
        }

        @Override
        public Type getMemberType(String member) {
            return memberTypes.get(member);
        }

        @Override
        public String getName() {
            return "instance-of-" + name;
        }
    }

    private final Map<String, Type> staticMembers = new HashMap<>();
    private final InstanceType instanceType = new InstanceType();
    private final String name;

    public ClassType(String name) {
        this.name = name;
    }

    public CompoundType getInstanceType() {
        return instanceType;
    }

    public void addMethod(String methodName, Type[] arguments, Type returnValue) {
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate instance method " + name + " in class " + this.name);
        instanceType.allMemberNames.add(methodName);
        instanceType.memberTypes.put(methodName, new FunctionType(name, this, false, arguments, returnValue));
    }

    public void addField(String fieldName, Type type) {
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate field " + name + " in class " + this.name);
        instanceType.allMemberNames.add(fieldName);
        instanceType.fieldNames.add(fieldName);
        instanceType.memberTypes.put(fieldName, type);
    }

    public void addStaticMethod(String methodName, Type[] arguments, Type returnValue) {
        if (staticMembers.containsKey(name))
            throw new IllegalArgumentException("Duplicate static method " + name + " in class " + this.name);
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException("Static method " + name + " collides with non static method in class " + this.name);

        staticMembers.put(methodName, new FunctionType(name, this, true, arguments, returnValue));
        instanceType.allMemberNames.add(methodName);
        instanceType.memberTypes.put(methodName, new FunctionType(name, this, false, arguments, returnValue));
    }

    //
    @Override
    public Collection<String> getMemberList() {
        return staticMembers.keySet();
    }

    @Override
    public Type getMemberType(String member) {
        return staticMembers.get(member);
    }

    @Override
    public String getName() {
        return name;
    }
}
