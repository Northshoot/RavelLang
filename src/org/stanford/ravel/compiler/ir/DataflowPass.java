package org.stanford.ravel.compiler.ir;

import org.stanford.ravel.compiler.ir.typed.TBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * An helper class to compute per-variable dataflow states
 *
 * Created by gcampagn on 2/8/17.
 */
public class DataflowPass<DataflowState> {
    // the state of the dataflow pass at the beginning or at the end of the corresponding
    // basic block
    private Map<TBlock, Map<Integer, DataflowState>> dataflowState = new HashMap<>();
}
