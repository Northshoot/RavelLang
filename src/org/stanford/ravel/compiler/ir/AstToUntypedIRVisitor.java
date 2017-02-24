package org.stanford.ravel.compiler.ir;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stanford.antlr4.RavelBaseVisitor;
import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ParserUtils;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.ir.untyped.*;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.Symbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.PrimitiveType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.stanford.ravel.compiler.ir.Registers.ERROR_REG;
import static org.stanford.ravel.compiler.ir.Registers.UNSET_REG;
import static org.stanford.ravel.compiler.ir.Registers.VOID_REG;

/**
 * Created by gcampagn on 1/20/17.
 */
class AstToUntypedIRVisitor extends RavelBaseVisitor<Integer> {
    private final UntypedIR ir;
    private final List<Block> blockStack = new ArrayList<>();

    private final CompileToIRPass compiler;
    private Scope currentScope;
    private int currentReg = VOID_REG;

    AstToUntypedIRVisitor(CompileToIRPass compiler, int firstGpRegister) {
        this.compiler = compiler;
        ir = new UntypedIR(firstGpRegister);
        blockStack.add(ir.getRoot());
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

    private void unreachable(Instruction instr) {
        compiler.emitWarning(new SourceLocation(instr.definer), "unreachable code");
    }

    private void addCurrent(Instruction instr) {
        Block current = blockStack.get(blockStack.size()-1);

        if (!current.add(instr))
            unreachable(instr);
    }
    private void addPrevious(Instruction instr) {
        Block previous = blockStack.get(blockStack.size()-2);

        if (!previous.add(instr))
            unreachable(instr);
    }

    @Override
    public Integer visitBlock(RavelParser.BlockContext ctx) {
        Scope parentScope = currentScope;
        currentScope = ctx.scope;
        visitChildren(ctx);
        currentScope = parentScope;
        return VOID_REG;
    }

    @Override
    public Integer visitVar_decl(RavelParser.Var_declContext ctx) {
        List<RavelParser.Ident_declContext> lvalues = ctx.identifier_list().ident_decl();
        if (ctx.expressionList() == null)
            return VOID_REG;

        List<RavelParser.ExpressionContext> rvalues = ctx.expressionList().expression();

        if (rvalues.size() > lvalues.size()) {
            compiler.emitError(new SourceLocation(ctx), "number of initalizations must match the number of variables");
        }

        for (int i = 0; i < Math.max(lvalues.size(), rvalues.size()); i++) {
            RavelParser.Ident_declContext lvalue;
            if (i < lvalues.size())
                lvalue = lvalues.get(i);
            else
                lvalue = null;

            int rvalueReg;
            int varReg;

            if (lvalue != null) {
                String varName = lvalue.Identifier().getText();
                Symbol var = currentScope.resolve(varName);
                if (var == null || !(var instanceof VariableSymbol)) {
                    compiler.emitError(new SourceLocation(ctx), varName + " is not a variable");
                    varReg = ERROR_REG;
                } else if (!((VariableSymbol) var).isWritable()) {
                    compiler.emitError(new SourceLocation(ctx), "cannot assign to read only variable " + varName);
                    varReg = ERROR_REG;
                } else {
                    varReg = ensureVarRegister((VariableSymbol) var);
                }
            } else {
                varReg = ERROR_REG;
            }

            if (i > rvalues.size())
                continue;
            rvalueReg = visit(rvalues.get(i));

            addCurrent(new Move(ctx, varReg, rvalueReg));
        }

        return VOID_REG;
    }

    @Override
    public Integer visitAssignment(RavelParser.AssignmentContext ctx) {
        List<RavelParser.Lvalue_expressionContext> lvalues = ctx.lvalue().lvalue_expression();
        List<RavelParser.ExpressionContext> rvalues = ctx.expressionList().expression();

        if (lvalues.size() != rvalues.size()) {
            compiler.emitError(new SourceLocation(ctx), "number of assignments must match the number of expressions");
        }

        String assignOp = ctx.assign_op().getText();
        boolean isCompound = !assignOp.equals("=");
        String compound = isCompound ? assignOp.substring(0, assignOp.length()-1) : null;

        for (int i = 0; i < Math.max(lvalues.size(), rvalues.size()); i++) {
            RavelParser.Lvalue_expressionContext lvalue;
            if (i < lvalues.size())
                    lvalue = lvalues.get(i);
            else
                lvalue = null;

            int rvalueReg;
            int varReg = UNSET_REG;
            int objectReg = UNSET_REG;
            int arrayReg = UNSET_REG;
            int indexReg = UNSET_REG;

            if (lvalue == null) {
                varReg = ERROR_REG;
            } else if (lvalue.primary() == null) {
                String varName = lvalue.Identifier().getText();
                Symbol var = currentScope.resolve(varName);
                if (var == null || !(var instanceof VariableSymbol)) {
                    compiler.emitError(new SourceLocation(ctx), varName + " is not a variable");
                    varReg = ERROR_REG;
                } else if (!((VariableSymbol) var).isWritable()) {
                    compiler.emitError(new SourceLocation(ctx), "cannot assign to read only variable " + varName);
                    varReg = ERROR_REG;
                } else {
                    varReg = ensureVarRegister((VariableSymbol)var);
                }
            } else if (lvalue.expression() == null) {
                String fieldName = lvalue.Identifier().getText();

                objectReg = visit(lvalue.primary());
            } else {
                arrayReg = visit(lvalue.primary());
                indexReg = visit(lvalue.expression());
            }

            if (isCompound) {
                int lvalueReg;

                if (lvalue == null) {
                    lvalueReg = ERROR_REG;
                } else if (lvalue.primary() == null) {
                    lvalueReg = varReg;
                } else if (lvalue.expression() == null) {
                    String fieldName = lvalue.Identifier().getText();

                    lvalueReg = ir.allocateRegister();
                    addCurrent(new FieldLoad(ctx, lvalueReg, objectReg, fieldName));
                } else {

                    lvalueReg = ir.allocateRegister();
                    addCurrent(new ArrayLoad(ctx, lvalueReg, arrayReg, indexReg));
                }

                if (i < rvalues.size())
                    rvalueReg = visit(rvalues.get(i));
                else
                    rvalueReg = ERROR_REG;

                int tmpReg = ir.allocateRegister();
                addCurrent(new BinaryArithOp(ctx, tmpReg, lvalueReg, rvalueReg, BinaryOperation.forSymbol(compound)));
                rvalueReg = tmpReg;
            } else {
                if (i < rvalues.size())
                    rvalueReg = visit(rvalues.get(i));
                else
                    rvalueReg = ERROR_REG;
            }

            if (lvalue == null) {
                addCurrent(new Move(ctx, varReg, rvalueReg));
            } else if (lvalue.primary() == null) {
                addCurrent(new Move(ctx, varReg, rvalueReg));
            } else if (lvalue.expression() == null) {
                String fieldName = lvalue.Identifier().getText();

                addCurrent(new FieldStore(ctx, objectReg, fieldName, rvalueReg));
            } else {
                addCurrent(new ArrayLoad(ctx, arrayReg, indexReg, rvalueReg));
            }
        }

        return VOID_REG;
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

            addPrevious(new IfStatement(ctx, cond, iftrue, iffalse));
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
        Block head = pushBlock();
        int cond = this.visit(ctx.expression());
        popBlock();
        Block body = pushBlock();
        this.visit(ctx.block_stmt());
        popBlock();
        addCurrent(new WhileLoop(ctx, cond, head, body));
        return VOID_REG;
    }

    private Symbol lookupName(ParserRuleContext ctx, String name) {
        Symbol var = currentScope.resolve(name);
        if (var == null) {
            compiler.emitError(new SourceLocation(ctx), "undeclared variable " + name);

            VariableSymbol replacement = new VariableSymbol(name);
            replacement.setType(PrimitiveType.ERROR);
            replacement.setWritable(true);
            currentScope.define(replacement);
            return replacement;
        } else {
            return var;
        }
    }

    private int ensureVarRegister(VariableSymbol var) {
        if (var.getRegister() == UNSET_REG)
            var.setRegister(ir.allocateRegister());
        return var.getRegister();
    }

    private int visitVarRef(ParserRuleContext ctx, String name) {
        Symbol sym = lookupName(ctx, name);

        if (sym instanceof VariableSymbol) {
            VariableSymbol var = (VariableSymbol) sym;
            return ensureVarRegister(var);
        } else {
            compiler.emitError(new SourceLocation(ctx), name + " is not a variable in scope");
            return ERROR_REG;
        }
    }
    
    @Override public Integer visitAtom(RavelParser.AtomContext ctx) {
        if (ctx.Identifier() != null) {
            String name = ctx.Identifier().getText();

            return visitVarRef(ctx, name);
        } else if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else {
            // a literal or an array literal
            return visitChildren(ctx);
        }
    }

    @Override
    public Integer visitLiteral(RavelParser.LiteralContext ctx) {
        int reg = ir.allocateRegister();
        Object value = ParserUtils.literalToValue(ctx);

        addCurrent(new ImmediateLoad(ctx, reg, value));
        return reg;
    }
    
    @Override public Integer visitArray_literal(RavelParser.Array_literalContext ctx) {
        // TODO
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
        addCurrent(new FieldLoad(ctx, reg, currentReg, ctx.Identifier().getText()));
        return reg;
    }

    @Override public Integer visitArray_access(RavelParser.Array_accessContext ctx) {
        int saveCurrent = currentReg;
        currentReg = VOID_REG;
        int index = visit(ctx.expression());
        currentReg = saveCurrent;

        int reg = ir.allocateRegister();
        addCurrent(new ArrayLoad(ctx, reg, currentReg, index));
        return reg;
    }

    @Override public Integer visitMethod_call(RavelParser.Method_callContext ctx) {
        int saveCurrent = currentReg;
        currentReg = VOID_REG;

        List<RavelParser.ExpressionContext> args = ctx.expressionList() != null ? ctx.expressionList().expression() : Collections.emptyList();
        int[] arguments = new int[args.size()];

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = visit(args.get(i));
        }

        currentReg = saveCurrent;
        int reg = ir.allocateRegister();
        addCurrent(new MethodCall(ctx, reg, currentReg, ctx.Identifier().getText(), arguments));
        return reg;
    }

    @Override public Integer visitPower_exp(RavelParser.Power_expContext ctx) {
        if (ctx.unary_exp() != null) {
            int base = visit(ctx.primary());
            int exponent = visit(ctx.unary_exp());
            int reg = ir.allocateRegister();
            addCurrent(new BinaryArithOp(ctx, reg, base, exponent, BinaryOperation.POW));
            return reg;
        } else {
            return visit(ctx.primary());
        }
    }

    @Override public Integer visitUnary_exp(RavelParser.Unary_expContext ctx) {
        if (ctx.unary_op() != null) {
            int value = visit(ctx.unary_exp());
            int reg = ir.allocateRegister();
            addCurrent(new UnaryArithOp(ctx, reg, value, UnaryOperation.forSymbol(ctx.unary_op().getText())));
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
        addCurrent(new BinaryArithOp(definer, target, src1, src2, BinaryOperation.forSymbol(op)));
        return target;
    }

    private int visitBinaryOp(ParserRuleContext definer, ParserRuleContext lhs, ParserRuleContext rhs, ParserRuleContext op) {
        return visitBinaryOp(definer, lhs, rhs, op != null ? op.getText() : null);
    }

    @Override public Integer visitMult_exp(RavelParser.Mult_expContext ctx) {
        return visitBinaryOp(ctx, ctx.mult_exp(), ctx.unary_exp(), ctx.mult_op());
    }

    @Override public Integer visitAdd_exp(RavelParser.Add_expContext ctx) {
        return visitBinaryOp(ctx, ctx.add_exp(), ctx.mult_exp(), ctx.add_op());
    }

    @Override public Integer visitShift_exp(RavelParser.Shift_expContext ctx) {
        return visitBinaryOp(ctx, ctx.shift_exp(), ctx.add_exp(), ctx.shift_op());
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
            addCurrent(new ComparisonOp(ctx, target, src1, src2, ComparisonOperation.forSymbol(ctx.comp_op(0).getText())));
            return target;
        }

        int comp = ir.allocateRegister();
        addCurrent(new ComparisonOp(ctx, comp, src1, src2, ComparisonOperation.forSymbol(ctx.comp_op(0).getText())));
        src1 = src2;

        int n = blockStack.size();
        for (int i = 2; i < children.size(); i++) {
            Block iftrue = pushBlock();
            addPrevious(new IfStatement(ctx, comp, iftrue, new Block()));
            src2 = visit(ctx.bin_or_exp(i));
            addCurrent(new ComparisonOp(ctx, comp, src1, src2, ComparisonOperation.forSymbol(ctx.comp_op(i-1).getText())));
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
            addCurrent(new UnaryArithOp(ctx, reg, value, UnaryOperation.NOT));
            return reg;
        } else {
            return visit(ctx.comp_exp());
        }
    }

    @Override public Integer visitAnd_exp(RavelParser.And_expContext ctx) {
        if (ctx.and_exp() != null) {
            // We need to lower "a and b" to
            // r = a
            // if r then:
            //   r = b
            // (which shortcuts side effects when computing the b part)
            int reg = visit(ctx.and_exp());
            Block iftrue = pushBlock();
            int reg2 = visit(ctx.not_exp());
            addCurrent(new Move(ctx, reg, reg2));
            popBlock();
            addCurrent(new IfStatement(ctx, reg, iftrue, new Block()));
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
            addCurrent(new Move(ctx, reg, reg2));
            popBlock();
            addCurrent(new IfStatement(ctx, reg, new Block(), iffalse));
            return reg;
        } else {
            return visit(ctx.and_exp());
        }
    }

    @Override
    public Integer visitBreak_stmt(RavelParser.Break_stmtContext ctx) {
        addCurrent(new Break(ctx));
        return VOID_REG;
    }

    @Override
    public Integer visitContinue_stmt(RavelParser.Continue_stmtContext ctx) {
        addCurrent(new Continue(ctx));
        return VOID_REG;
    }

    @Override
    public Integer visitReturn_stmt(RavelParser.Return_stmtContext ctx) {
        if (ctx.expression() != null) {
            int reg = visit(ctx.expression());
            addCurrent(new Move(ctx, Registers.RETURN_REG, reg));
        }
        addCurrent(new Return(ctx));
        return VOID_REG;
    }

    @Override
    public Integer visitForStatement(RavelParser.ForStatementContext ctx) {
        Scope parentScope = currentScope;
        currentScope = ctx.scope;

        RavelParser.Ident_declContext decl = ctx.forControl().ident_decl();
        RavelParser.ExpressionContext expr = ctx.forControl().expression();

        String varName = decl.Identifier().getText();
        int varReg;

        Symbol var = currentScope.resolve(varName);
        if (var == null || !(var instanceof VariableSymbol)) {
            compiler.emitError(new SourceLocation(ctx), varName + " is not a variable");
            varReg = ERROR_REG;
        } else if (!((VariableSymbol) var).isWritable()) {
            compiler.emitError(new SourceLocation(ctx), "cannot assign to read only variable " + varName);
            varReg = ERROR_REG;
        } else {
            varReg = ensureVarRegister((VariableSymbol) var);
        }

        // Lower to
        // array = ...
        // for (int i = 0; i < array.length; i++) {
        //  var = array[i];
        //  ...
        // }
        //
        // and then to
        // int i = 0;
        // while (i < array.length) {
        //   var = array[i];
        //   ...
        //   i++;
        // }

        int arrayReg = visit(expr);
        int indexReg = ir.allocateRegister();

        // int i = 0;
        addCurrent(new ImmediateLoad(decl, indexReg, 0));

        // array lengths are constant so we compute them outside the loop
        int lengthReg = ir.allocateRegister();
        addCurrent(new FieldLoad(expr, lengthReg, arrayReg, "length"));

        // load 1 into a register, for later use
        int immediateOne = ir.allocateRegister();
        addCurrent(new ImmediateLoad(decl, immediateOne, 1));

        // while (...
        Block head = pushBlock();
        int cond = ir.allocateRegister();
        // i < array.length
        addCurrent(new ComparisonOp(decl, cond, indexReg, lengthReg, ComparisonOperation.LT));
        popBlock();
        // ) {
        Block body = pushBlock();
        // var = array[i];
        addCurrent(new ArrayLoad(decl, varReg, arrayReg, indexReg));
        // ...
        this.visit(ctx.block_stmt());
        // i++
        addCurrent(new BinaryArithOp(decl, indexReg, indexReg, immediateOne, BinaryOperation.ADD));
        popBlock();
        // }
        addCurrent(new WhileLoop(ctx, cond, head, body));

        currentScope = parentScope;
        return VOID_REG;
    }

    public void declare(VariableSymbol var) {
        ensureVarRegister(var);
    }
}
