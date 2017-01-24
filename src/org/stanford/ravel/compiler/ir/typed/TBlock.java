package org.stanford.ravel.compiler.ir.typed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TBlock {
    private final int id;
    private final Set<TBlock> predecessors = new HashSet<>();
    private final Set<TBlock> successors = new HashSet<>();
    private final List<TInstruction> instructions = new ArrayList<>();

    TBlock(int id) {
        this.id = id;
    }
    int getId() {
        return id;
    }

    void addPredecessor(TBlock other) {
        this.predecessors.add(other);
    }
    void addSuccessor(TBlock other) {
        this.successors.add(other);
    }

    Set<TBlock> getSuccessors() {
        return successors;
    }
    Set<TBlock> getPredecessors() {
        return predecessors;
    }

    public boolean isEmpty() {
        return instructions.isEmpty();
    }

    public void add(TInstruction instr) {
        instructions.add(instr);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("B");
        builder.append(id);
        builder.append(" pre:[");
        for (TBlock b : predecessors) {
            builder.append("B");
            builder.append(b.getId());
            builder.append(",");
        }
        builder.append("] succ:[");
        for (TBlock b : successors) {
            builder.append("B");
            builder.append(b.getId());
            builder.append(",");
        }
        builder.append("]\n");

        for (TInstruction instr : instructions) {
            builder.append(instr.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
