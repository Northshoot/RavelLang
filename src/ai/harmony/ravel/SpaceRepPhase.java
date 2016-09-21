package ai.harmony.ravel;

import ai.harmony.antlr4.RavelBaseListener;
import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.compiler.scope.GlobalScope;
import ai.harmony.ravel.compiler.scope.Scope;

import java.util.logging.Logger;

/**
 * Created by lauril on 9/7/16.
 */
public class SpaceRepPhase extends RavelBaseListener {
    private static Logger LOGGER = Logger.getLogger(SpaceRepPhase.class.getName());
    private GlobalScope globals;
    private Scope currentScope;
    private RavelApplication rApp;

    public SpaceRepPhase(GlobalScope globals, RavelApplication rApp) {
        this.globals = globals;
        this.rApp = rApp;
        currentScope = globals;
    }

    /**
     * NOTE: Space is build after all primitives are initialized,
     * because we want to link no to real objects and create applications
     * @param ctx
     */
    @Override
    public void enterSpaceScope(RavelParser.SpaceScopeContext ctx) {
        /**
         * Create a platform object
         */

        /**
         * Instantiate model
         */
        //do models exist in Ravel App?

        // pass arguments to the model

        /**
         * Instantiate controllers
         */
        //Do controllers exist in Ravel App?

        //pass arguments in the controller

        /**
         * does the platform provide sources ?
         */

        /**
         * Does the platform provides sinks
         */


    }
}
