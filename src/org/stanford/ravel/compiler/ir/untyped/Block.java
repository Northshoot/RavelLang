package org.stanford.ravel.compiler.ir.untyped;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcampagn on 1/20/17.
 */
public class Block {
    private List<Instruction> instructions = new ArrayList<>();

    void accept(InstructionVisitor visitor) {
        visitor.beginBlock(this);
        for (Instruction instr : instructions)
            instr.accept(visitor);
        visitor.endBlock(this);
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
