package org.stanford.ravel.compiler.ir.typed;

/**
 * An interface to visit typed instructions
 *
 * Created by gcampagn on 1/25/17.
 */
public interface TInstructionVisitor {
    void visit(TArrayLoad arrayLoad);
    void visit(TArrayStore arrayStore);
    void visit(TBinaryArithOp arithOp);
    void visit(TBreak breakInstr);
    void visit(TComparisonOp compOp);
    void visit(TContinue continueInstr);
    void visit(TConvert convert);
    void visit(TFieldLoad fieldLoad);
    void visit(TFieldStore fieldStore);
    void visit(TIfStatement ifStatement);
    void visit(TImmediateLoad immediateLoad);
    void visit(TMethodCall methodCall);
    void visit(TModelCreateCall modelCreateCall);
    void visit(TModelRecordLoad modelRecordLoad);
    void visit(TMove move);
    void visit(TUnaryArithOp arithOp);
}
