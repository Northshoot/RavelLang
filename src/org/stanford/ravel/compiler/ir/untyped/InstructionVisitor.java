package org.stanford.ravel.compiler.ir.untyped;

/**
 * Created by gcampagn on 1/20/17.
 */
public interface InstructionVisitor {
    default void visit(ArrayLoad instr) {

    }

    default void visit(ArrayStore instr) {

    }

    default void visit(BinaryArithOp instr) {

    }

    default void visit(ComparisonOp instr) {

    }

    default void visit(FieldLoad instr) {

    }

    default void visit(FieldStore instr) {

    }

    default void visit(MethodCall instr) {

    }

    default void visit(IfStatement instr) {

    }

    default void visit(ImmediateLoad instr) {

    }

    default void visit(Move instr) {

    }

    default void visit(UnaryArithOp instr) {

    }

    default void visit(WhileLoop instr) {

    }

    default void visit(Break instr) {

    }

    default void visit(Continue instr) {

    }

    default void visit(Return instr) {

    }

    default void visit(ExplicitCast instr) {

    }
}
