package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.compiler.ir.typed.TMethodCall;
import org.stanford.ravel.compiler.types.ModelType;
import org.stanford.ravel.primitives.*;

import java.util.*;

/**
 * Compute flows between different spaces
 *
 * Created by gcampagn on 1/31/17.
 */
public class FlowAnalysis {
    /**
     * Describes the kind of operations that a controller does on a model
     *
     * As it is typical of data flow analysis, this forms a lattice:
     *
     *        Read
     * Noop /      \ RW
     *      \      /
     *       Write
     *
     * And therefore it has a meet operation
     */
    private enum DataOperations {
        NOOP,
        READ,
        WRITE,
        READ_WRITE;

        public static DataOperations meet(DataOperations d1, DataOperations d2) {
            if (d1 == NOOP) // identity
                return d2;
            if (d2 == NOOP) // identiy
                return d1;
            if (d1 == READ_WRITE) // zero elem
                return d1;
            if (d2 == READ_WRITE) // zero elem
                return d2;
            // now d1 and d2 either both read, both write, or one read and one write
            if (d1 != d2)
                return READ_WRITE;
            return d1;
        }
    };

    private final RavelCompiler driver;
    private final RavelApplication app;
    private final Map<Model, Map<InstantiatedController, DataOperations>> dataflowState = new HashMap<>();

    private final Map<Model, Set<Flow>> allFlows = new HashMap<>();

    public FlowAnalysis(RavelCompiler driver, RavelApplication app) {
        this.driver = driver;
        this.app = app;
    }

    private void meetWith(Model m, InstantiatedController ic, DataOperations op) {
        DataOperations previous = dataflowState.get(m).get(ic);
        dataflowState.get(m).put(ic, DataOperations.meet(previous, op));
    }

    private void visitEventHandler(InstantiatedController ic, EventHandler handler) {
        // for now, and for simplicity, we assume that an instantiated
        // controller will write to a model if we ever see a call to
        // .save()

        handler.getBody().getControlFlowGraph().visitForward((block) -> {
            for (TInstruction instr : block) {
                // ignore anything but method calls
                if (!(instr instanceof TMethodCall))
                    continue;

                TMethodCall methodCall = (TMethodCall) instr;
                // ignore anything but calls to .save
                if (!methodCall.method.equals("save"))
                    continue;
                // ignore anything but calls to .save on a model
                if (!(methodCall.type.getOwner() instanceof ModelType))
                    continue;

                // get the model from the model type
                Model model = app.getModel(methodCall.type.getOwner().getName());
                meetWith(model, ic, DataOperations.WRITE);
            }
        });
    }

    private void dataflowVisitSpace(Space s) {
        for (InstantiatedController ic : s.getControllers()) {
            for (LinkedEvent event : ic) {
                // if controller subscribes to model.arrived, it reads the records
                // on that model
                // (even if it actually does not read them, because it needs to receive them)
                if (event.getComponent() instanceof InstantiatedModel) {
                    InstantiatedModel im = (InstantiatedModel) event.getComponent();
                    if (event.getHandler().getEventName().equals("arrived"))
                        meetWith(im.getBaseModel(), ic, DataOperations.READ);
                }

                // Analyze the controller code to see if any operations write to the model
                visitEventHandler(ic, event.getHandler());
            }
        }
    }

    private void computeReadersWriters(Model m) {
        for (Map.Entry<InstantiatedController, DataOperations> entry : dataflowState.get(m).entrySet()) {
            switch (entry.getValue()) {
                case READ:
                    m.addReader(entry.getKey());
                    break;
                case WRITE:
                    m.addWriter(entry.getKey());
                    break;
                case READ_WRITE:
                    m.addReader(entry.getKey());
                    m.addWriter(entry.getKey());
                    break;
                case NOOP:
                default:
                    break;
            }
        }
    }

    private void addFlow(Model m, InstantiatedController from, InstantiatedController to) {
        allFlows.get(m).add(new Flow(from, to, m));
    }

    private void computeFlows(Model m) {
        for (InstantiatedController reader : m.getReaders()) {
            for (InstantiatedController writer : m.getWriters()) {
                if (reader == writer)
                    continue;
                addFlow(m, writer, reader);
            }
        }
    }

    private static class CycleFinder {
        private final Map<InstantiatedController, List<Flow>> graph = new HashMap<>();
        private final Set<InstantiatedController> allSources = new HashSet<>();
        private final Set<InstantiatedController> visited = new HashSet<>();

        private final List<InstantiatedController> cycle = new ArrayList<>();

        public CycleFinder(Set<Flow> flows) {
            for (Flow f : flows) {
                InstantiatedController from = f.getSource();
                allSources.add(from);
                List<Flow> edges = graph.computeIfAbsent(from, (key) -> new ArrayList<>());
                edges.add(f);
            }
        }

        private boolean dfs(InstantiatedController node) {
            if (visited.contains(node)) // found the cycle
                return true;
            visited.add(node);
            for (Flow edge : graph.getOrDefault(node, Collections.emptyList())) {
                if (dfs(edge.getSink())) {
                    cycle.add(node);
                    return true;
                }
            }
            return false;
        }

        public List<InstantiatedController> find() {
            for (InstantiatedController source : allSources) {
                visited.clear();
                if (dfs(source)) {
                    // the cycle computed by the dfs is reversed
                    // (because it's more efficient to add at the end of the arraylist)
                    Collections.reverse(cycle);
                    return cycle;
                }
            }

            return null;
        }
    }

    private static List<InstantiatedController> findCycle(Set<Flow> flows) {
        CycleFinder finder = new CycleFinder(flows);
        return finder.find();
    }

    private void complainAboutCycle(Model m, List<InstantiatedController> cycle) {
        driver.emitError(null, "dataflow for streaming model " + m.getName() + " is cyclic");
        StringBuilder cycleStr = new StringBuilder();
        cycleStr.append("found cycle ");
        for (int i = 0; i < cycle.size() - 1; i++) {
            InstantiatedController ic = cycle.get(i);
            cycleStr.append(ic.getSpace().getName());
            cycleStr.append(".");
            cycleStr.append(ic.getVarName());
            cycleStr.append(" -> ");
        }
        InstantiatedController ic = cycle.get(cycle.size()-1);
        cycleStr.append(ic.getSpace().getName());
        cycleStr.append(".");
        cycleStr.append(ic.getVarName());
        driver.emitInfo(null, cycleStr.toString());
    }

    private void verifyFlows(Model m) {
        switch (m.getModelType()) {
            case LOCAL:
                // local models have no flows
                return;

            case REPLICATED:
                // replicated models require no flow analysis, because everyone is
                // reading and writing everything
                return;

            case STREAMING:
                // streaming models must have no cycles
                List<InstantiatedController> cycle = findCycle(allFlows.get(m));
                if (cycle != null)
                    complainAboutCycle(m, cycle);
                return;

            default:
                throw new AssertionError();
        }
    }

    private void assignStreaming(Model m) {
        for (Flow f : allFlows.get(m)) {
            f.getSource().getSpace().addOutboundFlow(f);
            f.getSink().getSpace().addInboundFlow(f);
            app.addFlow(f);
        }
    }

    private void assignReplicated(Model m) {
        for (Space s1 : app.getSpaces()) {
            if (!s1.hasModel(m))
                continue;
            for (InstantiatedController ic1 : s1.getControllers()) {
                for (Space s2 : app.getSpaces()) {
                    if (s1 == s2)
                        continue;
                    if (!s2.hasModel(m))
                        continue;
                    for (InstantiatedController ic2 : s2.getControllers()) {
                        Flow f = new Flow(ic1, ic2, m);
                        f.getSource().getSpace().addOutboundFlow(f);
                        f.getSink().getSpace().addInboundFlow(f);
                        app.addFlow(f);
                    }
                }
            }
        }
    }

    private void assignFlows(Model m) {
        switch (m.getModelType()) {
            case LOCAL:
                // local models have no flows
                return;

            case STREAMING:
                // streaming models have the minimum set of flows to respect the semantics
                // of the controllers
                assignStreaming(m);
                return;

            case REPLICATED:
                // replicated models have all possible flows between all possible pairs
                // of InstantiatedControllers in spaces that have this models, regardless
                // of what the analysis said
                //
                // they also have intra flows within spaces according to the analysis,
                // which we record mostly for completeness
                assignStreaming(m);
                assignReplicated(m);
                return;

            default:
        }


    }

    public void run() {
        // process each model individually
        // different models never flow into each other
        //
        // (for the purposes of this analysis)

        // prepare state
        for (Model m : app.getModels()) {
            dataflowState.put(m, new HashMap<>());
            allFlows.put(m, new HashSet<>());

            for (Space s : app.getSpaces()) {
                for (InstantiatedController ic : s.getControllers()) {
                    dataflowState.get(m).put(ic, DataOperations.NOOP);
                }
            }
        }

        // determine what operation each controller is doing
        for (Space s : app.getSpaces())
            dataflowVisitSpace(s);

        for (Model m : app.getModels()) {
            // determine for each model who is doing what
            computeReadersWriters(m);

            // compute all flows
            computeFlows(m);

            // verify that all flows are semantically correct
            verifyFlows(m);

            // assign flows to the corresponding space
            assignFlows(m);
        }
    }
}
