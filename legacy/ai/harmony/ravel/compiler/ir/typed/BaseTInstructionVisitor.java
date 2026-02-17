package ai.harmony.ravel.compiler.ir.typed;

/**
 * A convenient TInstructionVisitor that provides empty implementations
 * of all methods.
 *
 * Created by gcampagn on 1/31/17.
 */
public class BaseTInstructionVisitor implements TInstructionVisitor {
    @Override
    public void visit(TArrayLoad arrayLoad) {

    }

    @Override
    public void visit(TArrayStore arrayStore) {

    }

    @Override
    public void visit(TBinaryArithOp arithOp) {

    }

    @Override
    public void visit(TBreak breakInstr) {

    }

    @Override
    public void visit(TComparisonOp compOp) {

    }

    @Override
    public void visit(TContinue continueInstr) {

    }

    @Override
    public void visit(TConvert convert) {

    }

    @Override
    public void visit(TFieldLoad fieldLoad) {

    }

    @Override
    public void visit(TFieldStore fieldStore) {

    }

    @Override
    public void visit(TIfStatement ifStatement) {

    }

    @Override
    public void visit(TImmediateLoad immediateLoad) {

    }

    @Override
    public void visit(TMethodCall methodCall) {

    }

    @Override
    public void visit(TMove move) {

    }

    @Override
    public void visit(TUnaryArithOp arithOp) {

    }

    @Override
    public void visit(TIntrinsic intrinsic) {
        
    }

    @Override
    public void visit(TReturn returnInstr) {

    }
}
