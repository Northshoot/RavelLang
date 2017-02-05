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

    private void applyParametersAndProperties(ConfigurableComponent component, ParametrizedComponent instance, InstanceSymbol is) {
        for (Map.Entry<String, Object> entry : is.getParameterMap().entrySet()) {
            String pname = entry.getKey();
            Object pvalue = entry.getValue();
            Type type = ParserUtils.typeFromLiteral(pvalue);

            if (!component.hasParameter(pname)) {
                driver.emitError(new SourceLocation(is.getDefNode()), component.getName()  + " has no parameter " + pname);
            } else if (!component.getParameterType(pname).isAssignable(type)) {
                driver.emitError(new SourceLocation(is.getDefNode()), "cannot assign value of type " + type.getName() + " to parameter " + pname + " of type " +
                        component.getParameterType(pname).getName());
            } else {
                instance.setParam(pname, pvalue);
            }
        }
        boolean ok = true;
        // check that all parameters are set
        for (String param : component.getParameterNames()) {
            if (!instance.isParamSet(param)) {
                driver.emitError(new SourceLocation(is.getDefNode()), "missing value for parameter " + param);
                ok = false;
            }
        }
        if (!ok)
            return;
        component.applyProperties(instance);
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

        // instantiate interfaces
        ssb.getInterfaces().forEach((iName, is) -> {
            // get the interface
            String interfaceName = is.getInstanceName();
            Interface i = app.getInterface(interfaceName);
            if (i == null) {
                driver.emitError(new SourceLocation(is.getDefNode()), interfaceName + " does not refer to a valid interface");
                return;
            }

            // instantiate the interface on this space
            InstantiatedInterface iiface = i.instantiate(space, is.getParameterMap(), is.getName());
            applyParametersAndProperties(i, iiface, is);
            space.add(is.getName(), iiface);
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
            applyParametersAndProperties(m, im, is);
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
            Map<String, EventComponent> eventMap = new HashMap<>();
            eventMap.put("system", space.getSystemAPI());
            ictr.setParam("system", space.getSystemAPI());

            boolean ok = true;
            for (Map.Entry<String, Object> param : is.getParameterMap().entrySet()) {
                String pname = param.getKey();
                Object pvalue = param.getValue();

                Object value;
                Type type;
                if (pvalue instanceof InstanceSymbol) {
                    // must refer to a model
                    String instanceName = ((InstanceSymbol) pvalue).getInstanceName();
                    Model m = app.getModel(instanceName);
                    if (m == null) {
                        Interface i = app.getInterface(instanceName);
                        if (i == null) {
                            // this can only happen if we complained above
                            // (either it's an invalid model or an invalid interface)
                            value = null;
                            type = null;
                            ok = false;
                        } else {
                            InstantiatedInterface iiface = space.getInterface(((InstanceSymbol) pvalue).getName());
                            assert iiface != null;
                            assert i == iiface.getBaseInterface();
                            eventMap.put(pname, iiface);
                            value = iiface;
                            type = i.getType();
                        }
                    } else {
                        InstantiatedModel im = space.getModel(((InstanceSymbol) pvalue).getName());
                        assert im != null;
                        assert m == im.getBaseModel();
                        eventMap.put(pname, im);
                        type = m.getType();
                        value = im;
                    }
                } else {
                    type = ParserUtils.typeFromLiteral(pvalue);
                    value = pvalue;
                }

                if (!ctr.hasParameter(pname)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "controller " + ctr.getName() + " has no parameter " + pname);
                    ok = false;
                } else if (type != null && !ctr.getParameterType(pname).isAssignable(type)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "cannot assign value of type " + type.getName() + " to parameter " + pname + " of type " +
                        ctr.getParameterType(pname).getName());
                    ok = false;
                } else {
                    if (ictr.isParamSet(pname)) {
                        driver.emitError(new SourceLocation(is.getDefNode()), "duplicate assignment to parameter " + pname);
                    } else {
                        ictr.setParam(pname, value);
                    }
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
                EventComponent ec = eventMap.get(modelVar.getName());
                // we know modelVar is a parameter of ctr (from DefPhase), we know it is of model type (because we checked types in
                // DefPhase), we know the value we're passing is actually a model (because we just checked) and we know
                // that we're passing a value (because we just checked)
                assert ec != null;
                ictr.linkEvent(e, ec);
            }

            space.add(is.getName(), ictr);
        });

        // freeze the space to build the derived state
        space.freezeAll();
        return space;
    }
}
