package org.stanford.ravel;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.ParserUtils;
import org.stanford.ravel.compiler.SourceLocation;
import org.stanford.ravel.compiler.symbol.InstanceSymbol;
import org.stanford.ravel.compiler.symbol.ReferenceSymbol;
import org.stanford.ravel.compiler.symbol.SpaceSymbol;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gcampagn on 1/26/17.
 */
public class ModelControllerLinker {
    private final RavelCompiler driver;
    private final RavelApplication app;

    public ModelControllerLinker(RavelCompiler driver, RavelApplication app) {
        this.driver = driver;
        this.app = app;
    }

    public Space processSpace(SpaceSymbol ssb) {
        RavelParser.SpaceScopeContext ctx = (RavelParser.SpaceScopeContext) ssb.getDefNode();

        String name = ssb.getName();
        Space space  = new Space(name);

        //TODO: makes this static part of the process rather than hardcoded strings

        /** build platform */
        Map<String, ReferenceSymbol> prop = ssb.getPlatform();
        Platform.Builder p = new Platform.Builder();
        p.name(prop.get("language").getName());
        p.language(prop.get("language").getValue());
        p.template(prop.get("templates").getValue());
        p.system(prop.get("system").getValue());

        Platform platform = p.build();
        try {
            platform.realize();
        } catch(ClassNotFoundException|InstantiationException|IllegalAccessException|ClassCastException e) {
            System.err.println("Invalid platform for space " + name);
            return null;
        }
        space.setPlatform(platform);

        /*
        // build sinks
        Map<String, ReferenceSymbol> sinks = ssb.getSink();
        for (ReferenceSymbol re: sinks.values()) {
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if (reference.startsWith("platform.system.")) {
                space.add(identifier, new Sink(identifier, reference));
            } else {
                driver.emitError(new SourceLocation(re.getDefNode()), identifier + " refers to an unknown location "
                        + reference);
            }
        }
        // build sources
        Map<String, ReferenceSymbol> source = ssb.getSource();

        for (ReferenceSymbol re: source.values()) {
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if (reference.startsWith("platform.system.")) {
                space.add(identifier, new Source(identifier, reference));
            } else {
                driver.emitError(new SourceLocation(re.getDefNode()), identifier + " refers to an unknown location "
                        + reference);
            }
        }*/

        // instantiate sinks
        ssb.getSink().forEach((sName, rs) -> {
            // get the sink
            String sinkName = rs.getValue();
            Sink s = app.getSink(sinkName);
            if (s == null) {
                driver.emitError(new SourceLocation(rs.getDefNode()), sinkName + " does not refer to a valid sink");
                return;
            }

            // instantiate the sink on this space
            // sinks don't have parameters (but they are ParametrizedComponent for consistency)
            InstantiatedSink isink = s.instantiate(space, new HashMap<>(), rs.getName());
            space.add(rs.getName(), isink);
        });

        // instantiate sources
        ssb.getSource().forEach((sName, rs) -> {
            // get the source
            String sourceName = rs.getValue();
            Source s = app.getSource(sourceName);
            if (s == null) {
                driver.emitError(new SourceLocation(rs.getDefNode()), sourceName + " does not refer to a valid source");
                return;
            }

            // instantiate the source on this space
            // sinks don't have parameters (but they are ParametrizedComponent for consistency)
            InstantiatedSource isource = s.instantiate(space, new HashMap<>(), rs.getName());
            space.add(rs.getName(), isource);
        });

        // instantiate models
        ssb.getModels().forEach((mName, is) -> {
            // add model and set all the parameters to the parameter map

            // get the model
            String modelName = is.getInstanceName();
            Model m = app.getModel(modelName);
            if (m == null) {
                driver.emitError(new SourceLocation(is.getDefNode()), modelName + " does not refer to a valid model");
                return;
            }

            // instantiate the model on this space
            InstantiatedModel im = m.instantiate(space, is.getParameterMap(), is.getName());
            space.add(is.getName(), im);
        });

        // instantiate controllers
        ssb.getControllers().forEach((cName, is) -> {
            // get the controller
            String controllerName = is.getInstanceName();
            Controller ctr = app.getController(is.getInstanceName());
            if (ctr == null) {
                driver.emitError(new SourceLocation(is.getDefNode()), controllerName + " does not refer to a valid model");
                return;
            }

            InstantiatedController ictr = ctr.instantiate(space, is.getName());

            // set parameters
            Map<String, InstantiatedModel> modelMap = new HashMap<>();

            boolean ok = true;
            for (Map.Entry<String, Object> param : is.getParameterMap().entrySet()) {
                String pname = param.getKey();
                Object pvalue = param.getValue();

                Object value;
                Type type;
                if (pvalue instanceof InstanceSymbol) {
                    // must refer to a model
                    String modelName = ((InstanceSymbol) pvalue).getInstanceName();
                    Model m = app.getModel(modelName);
                    // if m is null, we already complained loudly above
                    assert m != null;
                    InstantiatedModel im = space.getModel(((InstanceSymbol) pvalue).getName());
                    assert im != null;
                    assert m == im.getBaseModel();
                    modelMap.put(pname, im);
                    type = m.getType();
                    value = im;
                } else {
                    type = ParserUtils.typeFromLiteral(pvalue);
                    value = pvalue;
                }

                if (!ctr.getParameterType(pname).isAssignable(type)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "cannot assign value of type " + type.getName() + " to a parameter of type " +
                        ctr.getParameterType(pname).getName());
                    ok = false;
                } else {
                    ictr.setParam(pname, value);
                }
            }

            // check that all parameters are set
            for (String param : ctr.getParameterNames()) {
                if (!ictr.isParamSet(param)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "missing value for parameter " + param);
                    ok = false;
                }
            }
            if (!ok)
                return;

            // link events
            for (EventHandler e : ctr) {
                VariableSymbol modelVar = e.getModelVar();
                InstantiatedModel m = modelMap.get(modelVar.getName());
                // we know modelVar is a parameter of ctr (from DefPhase), we know it is of model type (because we checked types in
                // DefPhase), we know the value we're passing is actually a model (because we just checked) and we know
                // that we're passing a value (because we just checked)
                assert m != null;
                ictr.linkEvent(e, m);
            }

            space.add(is.getName(), ictr);
        });

        // freeze the space to build the derived state
        space.freezeAll();
        return space;
    }
}
