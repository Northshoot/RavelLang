package org.stanford.ravel.analysis.security;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.primitives.ConcreteModel;
import org.stanford.ravel.primitives.Space;

/**
 * Apply the result of security analysis by transforming the IR and tagging the spaces
 * with the operations they need to perform
 *
 * Created by gcampagn on 2/21/17.
 */
public class SecurityTransformation {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private final boolean enableEncryption;
    private final boolean enableMAC;

    public SecurityTransformation(RavelCompiler driver, RavelApplication app, boolean debug, boolean disableEncryption, boolean disableMAC) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
        this.enableEncryption = !disableEncryption;
        this.enableMAC = !disableMAC;
    }

    public void run() {
        for (Space space : app.getSpaces()) {
            for (ConcreteModel im : space.getModels()) {
                // do something
            }
        }
    }
}
