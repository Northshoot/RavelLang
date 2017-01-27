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
import java.util.Objects;

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

        /** build sinks */
        Map<String, ReferenceSymbol> sinks = ssb.getSink();
        for (ReferenceSymbol re: sinks.values()) {
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if (reference.startsWith("platform.system.")) {
                space.add(identifier, new Sink(identifier, reference));
            } else {
                driver.emitError(new SourceLocation(re.getDefNode()), identifier + " referring to unknown location "
                        + reference);
            }
        }
        /** build sources */
        Map<String, ReferenceSymbol> source = ssb.getSource();

        for (ReferenceSymbol re: source.values()) {
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if (reference.startsWith("platform.system.")) {
                space.add(identifier, new Source(identifier, reference));
            } else {
                driver.emitError(new SourceLocation(re.getDefNode()), identifier + " referring to unknown location "
                        + reference);
            }
        }

        /** build models */

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
            InstantiatedModel im = m.instantiate(space, is.getParameterMap());
            space.add(is.getIdentifier(), im);
        });

        /** build controllers */
        ssb.getControllers().forEach((cName, is) -> {
            // get the controller
            String controllerName = is.getInstanceName();
            Controller ctr = app.getController(is.getInstanceName());
            if (ctr == null) {
                driver.emitError(new SourceLocation(is.getDefNode()), controllerName + " does not refer to a valid model");
                return;
            }

            InstantiatedController ictr = ctr.instantiate(space);

            // set parameters
            Map<String, Model> modelMap = new HashMap<>();

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
                    modelMap.put(pname, m);
                    type = m.getType();
                    value = m;
                } else {
                    type = ParserUtils.typeFromLiteral(pvalue);
                    value = pvalue;
                }

                if (!ctr.getParameterType(pname).isAssignable(type)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "cannot assign value of type " + type.getName() + " to a parameter of type " +
                        ctr.getParameterType(pname).getName());
                    ok = false;
                } else {
                    ictr.setParam(pname, pvalue);
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
            for (Event e : ctr) {
                VariableSymbol modelVar = e.getModelVar();
                Model m = modelMap.get(modelVar.getName());
                // we know modelVar is a parameter of ctr (from DefPhase), we know it is of model type (because we checked types in
                // DefPhase), we know the value we're passing is actually a model (because we just checked) and we know
                // that we're passing a value (because we just checked)
                assert m != null;
                ictr.linkEvent(e, m);
            }

            space.add(is.getIdentifier(), ictr);
        });

        return space;
    }
}
