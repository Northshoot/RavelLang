package ai.harmony.ravel.compiler.ir.typed;

import java.util.*;

/**
 * Created by gcampagn on 1/23/17.
 */
public class TBlock implements Iterable<TInstruction> {
    private final int id;
    private final Set<TBlock> predecessors = new HashSet<>();
    private final Set<TBlock> successors = new HashSet<>();
    private final List<TInstruction> instructions = new LinkedList<>();
    private int sequenceId;

    TBlock(int id) {
        this.id = id;
    }
    int getId() {
        return id;
    }

    void setSequenceId(int id) {
        this.sequenceId = id;
    }
    int getSequenceId() {
        return sequenceId;
    }

    public boolean isTopologicallyAfter(TBlock other) {
        assert other == this || this.sequenceId != other.sequenceId;
        return this.sequenceId > other.sequenceId;
    }

    public void addPredecessor(TBlock other) {
        this.predecessors.add(other);
    }
    public void addSuccessor(TBlock other) {
        this.successors.add(other);
    }

    public Set<TBlock> getSuccessors() {
        return successors;
    }
    public Set<TBlock> getPredecessors() {
        return predecessors;
    }

    public boolean isEmpty() {
        return instructions.isEmpty();
    }

    public void prepend(TInstruction instr) {
        instructions.add(0, instr);
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

    @Override
    public Iterator<TInstruction> iterator() {
        return instructions.iterator();
    }

    public ListIterator<TInstruction> listIterator() {
        return instructions.listIterator();
    }

    public ListIterator<TInstruction> listIteratorAtLast() {
        return instructions.listIterator(instructions.size());
    }

    TInstruction getLastInstruction() {
        if (instructions.isEmpty())
            return null;
        return instructions.get(instructions.size()-1);
    }

    public void destroy() {
        for (TBlock pred : predecessors)
            pred.successors.remove(this);
        for (TBlock succ : successors)
            succ.predecessors.remove(this);
    }
}
