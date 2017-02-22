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

    private void applyParametersAndProperties(ConfigurableComponent component, ComponentInstance instance, InstanceSymbol is) {
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
        // check that all parameters are set
        for (String param : component.getParameterNames()) {
            if (!instance.isParamSet(param)) {
                driver.emitError(new SourceLocation(is.getDefNode()), "missing value for parameter " + param);
            }
        }
    }

    public Space processSpace(SpaceSymbol ssb) {
        RavelParser.SpaceScopeContext ctx = (RavelParser.SpaceScopeContext) ssb.getDefNode();

        String name = ssb.getName();
        Space space  = new Space(ssb);

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

        // instantiate interfaces
        ssb.getInterfaces().forEach((iName, is) -> {
            // get the interface
            String interfaceName = is.getInstanceName();
            Interface i = app.getInterface(interfaceName);
            if (i == null) {
                driver.emitError(new SourceLocation(is.getDefNode()), interfaceName + " does not refer to a valid interface");
                return;
            }

            // see if we have build the interface already
            ConcreteInterface iiface = space.findInterface(i);
            if (iiface == null)
                iiface = i.instantiate(space);

            // instantiate the interface on this space
            ConcreteInterfaceInstance instance = new ConcreteInterfaceInstance(iiface, is.getName());
            applyParametersAndProperties(i, instance, is);
            space.add(is.getName(), instance);
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
            if (space.hasModel(m)) {
                driver.emitError(new SourceLocation(is.getDefNode()), modelName + " is already present on this space");
                return;
            }

            // instantiate the model on this space
            ConcreteModel im = m.instantiate(space);
            ConcreteModelInstance instance = new ConcreteModelInstance(im, is.getName());

            applyParametersAndProperties(m, instance, is);
            space.add(is.getName(), instance);
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

            ConcreteController ictr = space.findController(ctr);
            if (ictr == null)
                ictr = ctr.instantiate(space);

            ConcreteControllerInstance instance = new ConcreteControllerInstance(ictr, is.getName());

            // set parameters
            Map<String, EventComponentInstance> eventMap = new HashMap<>();
            eventMap.put("system", space.getSystemAPI());
            instance.setParam("system", space.getSystemAPI());

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
                            ConcreteInterfaceInstance iiface = space.getInterface(((InstanceSymbol) pvalue).getName());
                            assert iiface != null;
                            assert i == iiface.getComponent().getBaseInterface();
                            eventMap.put(pname, iiface);
                            value = iiface;
                            type = i.getType();
                        }
                    } else {
                        ConcreteModelInstance im = space.getModel(((InstanceSymbol) pvalue).getName());
                        assert im != null;
                        assert m == im.getComponent().getBaseModel();
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
                    if (instance.isParamSet(pname)) {
                        driver.emitError(new SourceLocation(is.getDefNode()), "duplicate assignment to parameter " + pname);
                    } else {
                        instance.setParam(pname, value);
                    }
                }
            }

            // check that all parameters are set
            for (String param : ctr.getParameterNames()) {
                if (!instance.isParamSet(param)) {
                    driver.emitError(new SourceLocation(is.getDefNode()), "missing value for parameter " + param);
                    ok = false;
                }
            }
            if (!ok)
                return;

            // link components
            for (EventComponentInstance ec : eventMap.values())
                ictr.linkComponent(ec.getComponent());

            // link events
            for (EventHandler e : ctr) {
                VariableSymbol modelVar = e.getModelVar();
                EventComponentInstance ec = eventMap.get(modelVar.getName());
                // we know modelVar is a parameter of ctr (from DefPhase), we know it is of model type (because we checked types in
                // DefPhase), we know the value we're passing is actually a model (because we just checked) and we know
                // that we're passing a value (because we just checked)
                assert ec != null;
                instance.linkEvent(e, ec);
            }

            space.add(is.getName(), instance);
        });

        // freeze the space to build the derived state
        space.freezeAll();
        return space;
    }
}
