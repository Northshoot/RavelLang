package org.stanford.ravel;

import org.stanford.antlr4.RavelBaseListener;
import org.stanford.antlr4.RavelParser;
import org.stanford.antlr4.RavelParser.VarAssignmentContext;
import org.stanford.ravel.compiler.scope.GlobalScope;
import org.stanford.ravel.compiler.scope.Scope;
import org.stanford.ravel.compiler.symbol.*;
import org.stanford.ravel.primitives.*;
import org.stanford.ravel.primitives.Fields.*;
import org.stanford.ravel.primitives.Fields.Field.Builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * TODO: move linking between to the ravelApp
 *
 * Created by lauril on 8/30/16.
 */
public class PrimitiveRepPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(PrimitiveRepPhase.class.getName());
    private final RavelApplication rApp;

    public PrimitiveRepPhase(RavelApplication rApp) {
        this.rApp = rApp;
    }

    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx){
        String name = ctx.Identifier().getText();
        SpaceSymbol ssb = (SpaceSymbol)ctx.scope;
        Space space  = new Space(ssb.getName());
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
            return;
        }

        space.add(platform);

        /** build sinks */
        Map<String, ReferenceSymbol> sinks = ssb.getSink();
        for(ReferenceSymbol re: sinks.values()){
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if(reference.startsWith("platform.system.")){
                space.add(identifier, new Sink(identifier, reference));
            } else {
                LOGGER.severe("Error processing sink with name: "
                        + identifier + " referring to unknown location "
                        + reference);
            }
        }
        /** build sources */
        Map<String, ReferenceSymbol> source = ssb.getSource();

        for(ReferenceSymbol re: source.values()){
            //TODO: is reference starting good?
            String identifier = re.getName();
            String reference = re.getValue();
            //must start with platform.system.
            if(reference.startsWith("platform.system.")){
                space.add(identifier, new Source(identifier, reference));
            }
        }

        /** build models */
        Map<String, InstanceSymbol> modelInst = ssb.getModels();
        //add model and set all the parameters to the parameter map
        for(String mName: modelInst.keySet()){
            //get the instance symbol
            InstanceSymbol is = modelInst.get(mName);
            //get the model
            Model m = rApp.getModel(is.getInstanceName());
            //set parameters
            Map<String, String> ismap = is.getParameterMap();
            for(Map.Entry<String, String> entry : ismap.entrySet()) {
                m.setParam(entry.getKey(), entry.getValue());
            }
            space.add(is.getIdentifier(), m);
        }

        /** build controllers */
        Map<String, InstanceSymbol> ctrInst = ssb.getControllers();

        //add model and set all the parameters to the parameter map
        for(String mName: ctrInst.keySet()){
            //get the instance symbol
            InstanceSymbol is = ctrInst.get(mName);
            System.out.println("Ctr: " + is.getName());
            //get the model
            Controller ctr = rApp.getController(is.getInstanceName());
            System.out.println("Controller: \n" +ctr);

            InstantiatedController ictr = new InstantiatedController(space, ctr);

            //set parameters
            Map<String, String> ismap = is.getParameterMap();
            for(Map.Entry<String, String> entry : ismap.entrySet()) {
                ictr.setParam(entry.getKey(), entry.getValue());
            }
            space.add(is.getIdentifier(), ictr);
        }

        rApp.addSpace(name, space);
    }

    /**
     * TODO: build views
     */
}
