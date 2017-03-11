package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ParserUtils;
import org.stanford.ravel.compiler.ir.typed.*;
import org.stanford.ravel.compiler.types.ArrayType;
import org.stanford.ravel.compiler.types.Type;

import java.util.*;

/**
 * Created by gcampagn on 2/15/17.
 */
public class ConstantFolding {
    private static class ConstantState {
        enum Level { UNDEF, MAYBE_CONSTANT, CONSTANT, NOT_A_CONSTANT };

        private final Level level;
        private final Object value;

        ConstantState(Level level, Object value) {
            this.level = level;
            this.value = value;
            assert level == Level.CONSTANT || value == null;
        }

        static ConstantState meet(ConstantState s1, ConstantState s2) {
            if (s1 == null && s2 == null)
                return new ConstantState(Level.UNDEF, null);
            if (s1 == s2)
                return s1;
            if (s1 == null)
                return s2;
            if (s2 == null)
                return s1;
            if (s1.level == Level.UNDEF)
                return s2;
            if (s2.level == Level.UNDEF)
                return s1;
            if (s1.level == Level.MAYBE_CONSTANT)
                return s2;
            if (s2.level == Level.MAYBE_CONSTANT)
                return s1;
            if (s1.level == Level.NOT_A_CONSTANT)
                return s1;
            if (s2.level == Level.NOT_A_CONSTANT)
                return s2;
            if (Objects.equals(s1.value, s2.value))
                return s1;
            return new ConstantState(Level.NOT_A_CONSTANT, null);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ConstantState that = (ConstantState) o;

            if (level != that.level) return false;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            int result = level.hashCode();
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            if (level == Level.CONSTANT)
                return "CONSTANT " + value;
            else
                return "" + level;
        }
    }
    private final Set<Integer> declaredVariables = new HashSet<>();
    private final Map<Integer, ConstantState> constantVariables = new HashMap<>();
    private final TypedIR ir;
    private boolean progress;
    private boolean madeChanges;

    public ConstantFolding(TypedIR ir) {
        this.ir = ir;

        declaredVariables.addAll(ir.getParameters());

        ir.getRegisterTypes().forEach((entry) -> {
            int reg = entry.getKey();
            Type type = entry.getValue();
            if (type instanceof ArrayType && ((ArrayType) type).isKnownBound())
                declaredVariables.add(reg);
        });
    }

    public boolean run() {
        madeChanges = false;
        constantVariables.clear();
        for (int reg : declaredVariables)
            constantVariables.put(reg, new ConstantState(ConstantState.Level.NOT_A_CONSTANT, null));

        // first compute the constants
        do {
            progress = false;
            ir.getControlFlowGraph().visitForward(this::visitAnalysis);
        } while(progress);

        // then replace the instructions with immediate loads
        ir.getControlFlowGraph().visitForward(this::visitEliminate);
        return madeChanges;
    }

    private void meetWith(int var, ConstantState.Level level) {
        constantVariables.compute(var, (key, existing) -> {
            ConstantState computed = ConstantState.meet(existing, new ConstantState(level, null));
            if (!Objects.equals(computed, existing))
                progress = true;
            return computed;
        });
    }

    private void meetWith(int var, Object value) {
        constantVariables.compute(var, (key, existing) -> {
            ConstantState computed = ConstantState.meet(existing, new ConstantState(ConstantState.Level.CONSTANT, value));
            if (!Objects.equals(computed, existing))
                progress = true;
            return computed;
        });
    }

    private void visitAnalysis(TBlock block) {
        for (TInstruction instr : block) {
            if (instr.getSink() == Registers.VOID_REG)
                continue;

            int[] sources = instr.getSources();
            if (instr instanceof TPhi) {
                // phis must be treated specially
                for (int source : sources) {
                    ConstantState state = constantVariables.get(source);
                    if (state == null || state.level == ConstantState.Level.UNDEF)
                        continue;
                    if (state.level == ConstantState.Level.CONSTANT)
                        meetWith(instr.getSink(), state.value);
                    else
                        meetWith(instr.getSink(), state.level);
                }

                continue;
            }

            Object[] srcValues = new Object[sources.length];

            boolean isAnyUndef = false;
            boolean isAllUndef = sources.length > 0;
            boolean isNac = false;
            for (int i = 0; i < sources.length; i++) {
                ConstantState state = constantVariables.get(sources[i]);
                if (state == null || state.level == ConstantState.Level.UNDEF) {
                    isAnyUndef = true;
                } else {
                    isAllUndef = false;
                    if (state.level == ConstantState.Level.NOT_A_CONSTANT) {
                        isNac = true;
                        break;
                    } else if (state.level == ConstantState.Level.MAYBE_CONSTANT) {
                        isAnyUndef = true;
                    } else {
                        srcValues[i] = state.value;
                    }
                }
            }
            if (isNac) {
                meetWith(instr.getSink(), ConstantState.Level.NOT_A_CONSTANT);
            } else if (isAllUndef) {
                meetWith(instr.getSink(), ConstantState.Level.UNDEF);
            } else if (isAnyUndef) {
                meetWith(instr.getSink(), ConstantState.Level.MAYBE_CONSTANT);
            } else {
                Object result = instr.evaluate(srcValues);
                if (result == null)
                    meetWith(instr.getSink(), ConstantState.Level.NOT_A_CONSTANT);
                else
                    meetWith(instr.getSink(), result);
            }
        }
    }

    private void visitEliminate(TBlock block) {
        ListIterator<TInstruction> iter = block.listIterator();
        while (iter.hasNext()) {
            TInstruction instr = iter.next();
            if (instr instanceof TImmediateLoad)
                continue;
            if (instr.getSink() == Registers.VOID_REG)
                continue;

            ConstantState state = constantVariables.get(instr.getSink());
            // MAYBE_CONSTANT means that we saw a definition of this variable, and at least
            // one value was not UNDEF, but at least one value was UNDEF and that survived
            // until the end
            // in this case we're free to remove the instruction altoghether
            if (state == null || state.level == ConstantState.Level.UNDEF || state.level == ConstantState.Level.MAYBE_CONSTANT)
                iter.remove();
            else if (state.level == ConstantState.Level.CONSTANT)
                iter.set(new TImmediateLoad(ParserUtils.typeFromLiteral(state.value), instr.getSink(), state.value));
        }
    }
}
