package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.symbol.FlowSymbol;
import org.stanford.ravel.compiler.symbol.ModelSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.primitives.Flow;
import org.stanford.ravel.primitives.InstantiatedModel;
import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.Space;

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

        // build a flow from every space to every space
        for (Space s1 : spaces) {
            for (Space s2 : spaces) {
                if (s1 == s2)
                    continue;

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
        for (InstantiatedModel im : s.getModels()) {
            Collection<Flow> flows = im.getBaseModel().findFlowsForSpace(s);

            if (flows.isEmpty()) {
                driver.emitError(new SourceLocation(s.getSymbol().getDefNode()), "space " + s.getName() + " is not included in flow for " + im.getName());
                continue;
            }

            for (Flow f : flows) {
                Space next = f.getNext(s);
                Space previous = f.getPrevious(s);

                if (next != null)
                    im.addStreamingSink(next);
                if (previous != null)
                    im.addStreamingSource(previous);
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
