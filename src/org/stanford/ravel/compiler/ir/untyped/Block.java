package org.stanford.ravel.compiler.ir.untyped;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class Block {
    private final List<Instruction> instructions = new ArrayList<>();

    public void accept(InstructionVisitor visitor) {
        for (Instruction instr : instructions)
            instr.accept(visitor);
    }

    public void add(Instruction instr) {
        instructions.add(instr);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Instruction instr : instructions) {
            builder.append(instr.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
