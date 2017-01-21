package org.stanford.ravel.compiler.ir;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.antlr4.RavelBaseVisitor;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ControllerCompiler;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.untyped.*;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.ERROR_REG;
import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.UNSET_REG;
import static org.stanford.ravel.compiler.ir.untyped.UntypedIR.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
public class AstToUntypedIRVisitor extends RavelBaseVisitor<Integer> {
    private final UntypedIR ir = new UntypedIR();
    private final List<Block> blockStack = new ArrayList<>();

    private final ControllerCompiler compiler;
    private Scope currentScope;
    private int currentReg = VOID_REG;

    public AstToUntypedIRVisitor(ControllerCompiler compiler) {
        this.compiler = compiler;
    }

    public UntypedIR getIR() {
        return ir;
    }

    private Block pushBlock() {
        Block block = new Block();
        blockStack.add(block);
        return block;
    }

    private void popBlock() {
        blockStack.remove(blockStack.size()-1);
    }

    private Block current() {
        return blockStack.get(blockStack.size()-1);
    }

    private Block previous() {
        return blockStack.get(blockStack.size()-1);
    }

    @Override
    public Integer visitIfStatement(RavelParser.IfStatementContext ctx) {
        // lower elifs to an if/else tree

        List<RavelParser.ExpressionContext> conditions = ctx.expression();
        List<RavelParser.Block_stmtContext> bodies = ctx.block_stmt();

        assert conditions.size() == bodies.size() || conditions.size() == bodies.size()-1;

        int n = blockStack.size();

        for (int i = 0; i < conditions.size(); i++) {
            int cond = this.visit(conditions.get(i));

            Block iftrue = pushBlock();
            this.visit(bodies.get(i));
            popBlock();
            Block iffalse = pushBlock();

            previous().add(new IfStatement(ctx, cond, iftrue, iffalse));
        }
        if (bodies.size() == conditions.size()+1)
            this.visit(bodies.get(bodies.size()-1));
        for (int i = 0; i < conditions.size(); i++) {
            popBlock();
        }

        assert blockStack.size() == n;
        return VOID_REG;
    }

    @Override
    public Integer visitWhileStatement(RavelParser.WhileStatementContext ctx) {
        int cond = this.visit(ctx.expression());
        Block body = pushBlock();
        this.visit(ctx.block_stmt());
        popBlock();
        current().add(new WhileLoop(ctx, cond, body));
        return VOID_REG;
    }

    private Symbol lookupName(ParserRuleContext ctx, String name) {
        Symbol var = currentScope.resolve(name);
        if (var == null) {
            compiler.emitError(new SourceLocation(ctx), "undeclared variable " + name);

            VariableSymbol replacement = new VariableSymbol(name);
            replacement.setType(PrimitiveType.ERROR);
            currentScope.define(replacement);
            return replacement;
        } else {
            return var;
        }
    }
    
    @Override public Integer visitAtom(RavelParser.AtomContext ctx) {
        if (ctx.Identifier() != null) {
            String name = ctx.Identifier().getText();

            Symbol sym = lookupName(ctx, name);

            if (sym instanceof VariableSymbol) {
                VariableSymbol var = (VariableSymbol) sym;
                if (var.getRegister() == UNSET_REG)
                    var.setRegister(ir.allocateRegister());
                return var.getRegister();
            } else {
                // probably a model or a function
                // allocate a pseudo register to load this, and let type check
                // eliminate it
                int reg = ir.allocateRegister();
                current().add(new SymbolLoad(ctx, reg, sym));
                return reg;
            }

        } else {
            // a literal or an array literal
            return visitChildren(ctx);
        }
    }

    @Override
    public Integer visitLiteral(RavelParser.LiteralContext ctx) {
        int reg = ir.allocateRegister();
        current().add(new ImmediateLoad(ctx, reg, ctx));
        return reg;
    }
    
    @Override public Integer visitArray_literal(RavelParser.Array_literalContext ctx) {
        // FINISHME
        return ERROR_REG;
    }

    @Override public Integer visitPrimary(RavelParser.PrimaryContext ctx) {
        int reg = visit(ctx.atom());

        currentReg = reg;
        for (RavelParser.Access_opContext access_opContext : ctx.access_op()) {
            currentReg = visit(access_opContext);
        }

        reg = currentReg;
        currentReg = VOID_REG;
        return reg;
    }

    @Override public Integer visitMember_access(RavelParser.Member_accessContext ctx) {
        int reg = ir.allocateRegister();
        current().add(new FieldLoad(ctx, reg, currentReg, ctx.Identifier().getText()));
        return reg;
    }

    @Override public Integer visitArray_access(RavelParser.Array_accessContext ctx) {
        int saveCurrent = currentReg;
        currentReg = VOID_REG;
        int index = visit(ctx.expression());
        currentReg = saveCurrent;

        int reg = ir.allocateRegister();
        current().add(new ArrayLoad(ctx, reg, currentReg, index));
        return reg;
    }

    @Override public Integer visitFunction_call(RavelParser.Function_callContext ctx) {
        int saveCurrent = currentReg;
        currentReg = VOID_REG;

        List<RavelParser.ExpressionContext> args = ctx.expressionList().expression();
        int[] arguments = new int[args.size()];

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = visit(args.get(i));
        }

        currentReg = saveCurrent;
        int reg = ir.allocateRegister();
        current().add(new FunctionCall(ctx, reg, currentReg, arguments));
        return reg;
    }

    @Override public Integer visitPower_exp(RavelParser.Power_expContext ctx) {
        if (ctx.unary_exp() != null) {
            int base = visit(ctx.primary());
            int exponent = visit(ctx.unary_exp());
            int reg = ir.allocateRegister();
            current().add(new BinaryArithOp(ctx, reg, base, exponent, "**"));
            return reg;
        } else {
            return visit(ctx.primary());
        }
    }

    @Override public Integer visitUnary_exp(RavelParser.Unary_expContext ctx) {
        if (ctx.unary_op() != null) {
            int value = visit(ctx.unary_exp());
            int reg = ir.allocateRegister();
            current().add(new UnaryArithOp(ctx, reg, value, ctx.unary_op().getText()));
            return reg;
        } else {
            return visit(ctx.power_exp());
        }
    }

    private int visitBinaryOp(ParserRuleContext definer, ParserRuleContext lhs, ParserRuleContext rhs, String op) {
        if (lhs == null)
            return visit(rhs);

        int src1 = visit(lhs);
        int src2 = visit(rhs);
        int target = ir.allocateRegister();
        current().add(new BinaryArithOp(definer, target, src1, src2, op));
        return target;
    }

    @Override public Integer visitMult_exp(RavelParser.Mult_expContext ctx) {
        return visitBinaryOp(ctx, ctx.mult_exp(), ctx.unary_exp(), ctx.mult_op().getText());
    }

    @Override public Integer visitAdd_exp(RavelParser.Add_expContext ctx) {
        return visitBinaryOp(ctx, ctx.add_exp(), ctx.mult_exp(), ctx.add_op().getText());
    }

    @Override public Integer visitShift_exp(RavelParser.Shift_expContext ctx) {
        return visitBinaryOp(ctx, ctx.shift_exp(), ctx.add_exp(), ctx.shift_op().getText());
    }

    @Override public Integer visitBin_and_exp(RavelParser.Bin_and_expContext ctx) {
        return visitBinaryOp(ctx, ctx.bin_and_exp(), ctx.shift_exp(), "&");
    }

    @Override public Integer visitBin_xor_exp(RavelParser.Bin_xor_expContext ctx) {
        return visitBinaryOp(ctx, ctx.bin_xor_exp(), ctx.bin_and_exp(), "^");
    }

    @Override public Integer visitBin_or_exp(RavelParser.Bin_or_expContext ctx) {
        return visitBinaryOp(ctx, ctx.bin_or_exp(), ctx.bin_xor_exp(), "|");
    }

    @Override public Integer visitComp_exp(RavelParser.Comp_expContext ctx) {
        List<RavelParser.Bin_or_expContext> children = ctx.bin_or_exp();
        if (children.size() == 1)
            return visit(children.get(0));

        int src1 = visit(children.get(0));
        int src2 = visit(children.get(1));
        if (children.size() == 2) {
            // fast path common case of one comparison
            int target = ir.allocateRegister();
            current().add(new BinaryArithOp(ctx, target, src1, src2, ctx.comp_op(0).getText()));
            return target;
        }

        int comp = ir.allocateRegister();
        current().add(new BinaryArithOp(ctx, comp, src1, src2, ctx.comp_op(0).getText()));
        src1 = src2;

        int n = blockStack.size();
        for (int i = 2; i < children.size(); i++) {
            Block iftrue = pushBlock();
            previous().add(new IfStatement(ctx, comp, iftrue, new Block()));
            src2 = visit(ctx.bin_or_exp(i));
            current().add(new BinaryArithOp(ctx, comp, src1, src2, ctx.comp_op(i-1).getText()));
            src1 = src2;
        }
        for (int i = 2; i < children.size(); i++)
            popBlock();

        assert blockStack.size() == n;
        return comp;
    }

    @Override public Integer visitNot_exp(RavelParser.Not_expContext ctx) {
        if (ctx.not_exp() != null) {
            int value = visit(ctx.not_exp());
            int reg = ir.allocateRegister();
            current().add(new UnaryArithOp(ctx, reg, value, "!"));
            return reg;
        } else {
            return visit(ctx.comp_exp());
        }
    }

    @Override public Integer visitAnd_exp(RavelParser.And_expContext ctx) {
        if (ctx.and_exp() != null) {
            int reg = visit(ctx.and_exp());
            Block iftrue = pushBlock();
            int reg2 = visit(ctx.not_exp());
            current().add(new Move(ctx, reg, reg2));
            popBlock();
            current().add(new IfStatement(ctx, reg, iftrue, new Block()));
            return reg;
        } else {
            return visit(ctx.not_exp());
        }
    }

    @Override public Integer visitOr_exp(RavelParser.Or_expContext ctx) {
        if (ctx.or_exp() != null) {
            int reg = visit(ctx.or_exp());
            Block iffalse = pushBlock();
            int reg2 = visit(ctx.and_exp());
            current().add(new Move(ctx, reg, reg2));
            popBlock();
            current().add(new IfStatement(ctx, reg, new Block(), iffalse));
            return reg;
        } else {
            return visit(ctx.and_exp());
        }
    }
}
