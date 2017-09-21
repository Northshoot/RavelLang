package ai.harmony.ravel.analysis;

import ai.harmony.ravel.RavelApplication;
import ai.harmony.ravel.RavelCompiler;
import ai.harmony.ravel.compiler.SourceLocation;
import ai.harmony.ravel.compiler.symbol.FlowSymbol;
import ai.harmony.ravel.compiler.symbol.ModelSymbol;
import ai.harmony.ravel.compiler.symbol.SpaceSymbol;
import ai.harmony.ravel.primitives.*;

import java.util.*;

/**
 * Compute flows between different spaces
 *
 * Created by gcampagn on 1/31/17.
 */
public class FlowAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;

    public FlowAnalysis(RavelCompiler driver, RavelApplication app) {
        this.driver = driver;
        this.app = app;
    }

    private void runLocal(Model m) {
        ModelSymbol ms = m.getSymbol();

        FlowSymbol flowSym = (FlowSymbol) ms.getNestedScope("properties").resolve("flow");
        if (flowSym != null)
            driver.emitError(new SourceLocation(flowSym.getDefNode()), "local models cannot define flows");

        // build fake flows from one space to itself
        for (Space s : app.getSpaces()) {
            if (s.hasModel(m)) {
                Flow f = new Flow(Arrays.asList(s, s), m);
                app.addFlow(f);
                m.addFlow(f);
            }
        }
    }

    private List<Space> makeSpaceList(FlowSymbol flowSym) {
        List<Space> spaces = new ArrayList<>();
        Set<Space> dupes = new HashSet<>();
        for (String spaceName : flowSym.getSpaces()) {
            Space space = app.getSpace(spaceName);
            if (space == null) {
                driver.emitError(new SourceLocation(flowSym.getDefNode()), "undefined space " + spaceName);
                // make up a space on the fly
                space = new Space(new SpaceSymbol(spaceName));
                app.addSpace(spaceName, space);
            }
            if (dupes.contains(space)) {
                driver.emitError(new SourceLocation(flowSym.getDefNode()), "duplicate space " + spaceName);
            }
            dupes.add(space);
            spaces.add(space);
        }

        return spaces;
    }

    private boolean writesTo(Space s, Model m) {
        for (ConcreteController ic : s.getControllers()) {
            if (ic.getController().writesTo(m))
                return true;
        }
        return false;
    }

    private void runStreaming(Model m) {
        ModelSymbol ms = m.getSymbol();

        FlowSymbol flowSym = (FlowSymbol) ms.getNestedScope("properties").resolve("flow");
        if (flowSym == null) {
            driver.emitError(new SourceLocation(ms.getDefNode()), "streaming models must define a directed flow");
            return;
        }

        if (flowSym.getSpaces().size() < 2) {
            driver.emitError(new SourceLocation(flowSym.getDefNode()), "a flow must involve at least 2 spaces");
            return;
        }

        if (!flowSym.isDirected()) {
            driver.emitError(new SourceLocation(flowSym.getDefNode()), "a streaming model must use a directed flow");
            return;
        }

        List<Space> spaces = makeSpaceList(flowSym);

        // build a single directed flow
        Flow flow = new Flow(spaces, m);

        if (!writesTo(flow.getSource(), m)) {
            driver.emitWarning(new SourceLocation(flowSym.getDefNode()), "space " + flow.getSource() + " is the source of a streaming flow for model " + m.getName() + " but does not write into it");
        }

        m.addFlow(flow);
        app.addFlow(flow);
    }

    private void runReplicated(Model m) {
        ModelSymbol ms = m.getSymbol();

        FlowSymbol flowSym = (FlowSymbol) ms.getNestedScope("properties").resolve("flow");

        List<Space> spaces;
        if (flowSym != null) {
            if (flowSym.getSpaces().size() < 2) {
                driver.emitError(new SourceLocation(flowSym.getDefNode()), "a flow must involve at least 2 spaces");
                return;
            }

            if (flowSym.isDirected()) {
                driver.emitError(new SourceLocation(flowSym.getDefNode()), "a streaming model must use an undirected flow");
                return;
            }

            spaces = makeSpaceList(flowSym);
        } else {
            spaces = new ArrayList<>();

            // add all spaces that include this model
            for (Space s : app.getSpaces()) {
                if (s.hasModel(m))
                    spaces.add(s);
            }
        }

        // build a flow from every writing space to every other space
        for (Space s1 : spaces) {
            if (!writesTo(s1, m))
                continue;

            for (Space s2 : spaces) {
                Flow flow = new Flow(Arrays.asList(s1, s2), m);
                m.addFlow(flow);
                app.addFlow(flow);
            }
        }
    }

    private void verify(Model m) {
        for (Flow f : m.getFlows()) {
            for (Space s : f) {
                if (!s.hasModel(m)) {
                    driver.emitError(new SourceLocation(s.getSymbol().getDefNode()), "space " + s.getName() + " must instantiate model " + m.getName() + " to satisfy flow from " + f.getSource() + " to " + f.getSink());
                }
            }
        }
    }

    private void verify(Space s) {
        for (ConcreteModel im : s.getModels()) {
            Collection<Flow> flows = im.getBaseModel().findFlowsForSpace(s);

            if (flows.isEmpty()) {
                driver.emitError(new SourceLocation(s.getSymbol().getDefNode()), "space " + s.getName() + " is not included in flow for " + im.getName());
                continue;
            }

            for (Flow f : flows) {
                Space next = f.getNext(s);
                Space previous = f.getPrevious(s);

                if (next != null && next != s)
                    im.addStreamingSink(next);
                if (previous != null && previous != s)
                    im.addStreamingSource(previous);
            }
        }

        for (ConcreteController ic : s.getControllers()) {
            for (Model m : ic.getController().getWrittenToModels()) {
                boolean found = false;
                for (Flow f : m.getFlows()) {
                    if (f.getSource() == s) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    driver.emitError(new SourceLocation(s.getSymbol().getDefNode()), "Controller " + ic.getName() + " in space " + s.getName() + " writes to model " + m.getName() + " but the space is not a source for any model flow");
                }
            }
        }
    }

    public void run() {
        // process each model individually
        // different models never flow into each other
        //
        // (for the purposes of this analysis)

        for (Model m : app.getModels()) {
            switch (m.getModelType()) {
                case LOCAL:
                    runLocal(m);
                    break;
                case STREAMING:
                    runStreaming(m);
                    break;
                case REPLICATED:
                    runReplicated(m);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        if (!driver.success())
            return;
        for (Model m : app.getModels()) {
            verify(m);
        }
        if (!driver.success())
            return;

        for (Space s : app.getSpaces()) {
            verify(s);
        }
    }
}
