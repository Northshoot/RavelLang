package org.stanford.ravel.compiler.types;

import java.util.*;

/**
 * The base type of all class types
 *
 * Created by gcampagn on 1/23/17.
 */
public class ClassType implements CompoundType {
    public class InstanceType implements CompoundType {
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
        public boolean isWritable(String member) {
            return fieldNames.contains(member);
        }

        @Override
        public String getName() {
            return "instance-of-" + name;
        }

        public ClassType getClassType() {
            return ClassType.this;
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

    void addMethod(String methodName, Type[] arguments, Type returnValue) {
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate instance method " + name + " in class " + this.name);
        instanceType.allMemberNames.add(methodName);
        instanceType.memberTypes.put(methodName, new FunctionType(methodName, this, false, arguments, returnValue));
    }

    void addField(String fieldName, Type type) {
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException("Duplicate field " + name + " in class " + this.name);
        instanceType.allMemberNames.add(fieldName);
        instanceType.fieldNames.add(fieldName);
        instanceType.memberTypes.put(fieldName, type);
    }

    private void addStaticMember(String memberName, Type type, String what) {
        if (staticMembers.containsKey(name))
            throw new IllegalArgumentException(what + " " + name + " collides with static method in class " + this.name);
        if (instanceType.memberTypes.containsKey(name))
            throw new IllegalArgumentException(what + " " + name + " collides with non static method in class " + this.name);

        staticMembers.put(memberName, type);
        instanceType.allMemberNames.add(memberName);
        instanceType.memberTypes.put(memberName, type);
    }

    void addStaticMethod(String methodName, Type[] arguments, Type returnValue) {
        FunctionType functionType = new FunctionType(methodName, this, true, arguments, returnValue);
        addStaticMember(methodName, functionType, "Static method");
    }

    void addEvent(String eventName, Type[] arguments, boolean hasSelf) {
        EventType event = new EventType(new FunctionType(eventName, this, true, arguments, PrimitiveType.VOID), hasSelf);
        addStaticMember(eventName, event, "Event");
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
    public boolean isWritable(String member) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
