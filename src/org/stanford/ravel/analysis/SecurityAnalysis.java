package org.stanford.ravel.analysis;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.primitives.Model;
import org.stanford.ravel.primitives.ModelField;
import org.stanford.ravel.primitives.Space;

import java.util.*;

/**
 * Compute, for each space, what it needs to do security-wise for each field in each
 * model it instantiates (including which keys are each field encrypted with, which
 * keys they are mac'ed with etc).
 *
 * Created by gcampagn on 2/13/17.
 */
public class SecurityAnalysis {
    // A Key is represented a set of spaces that have some shared secret
    public static class Key {
        public final Set<Space> spaces = new HashSet<>();

        Key(Collection<Space> spaces) {
            this.spaces.addAll(spaces);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            return spaces.equals(key.spaces);
        }

        @Override
        public int hashCode() {
            return spaces.hashCode();
        }
    }

    // A security operation is a pair of a security primitive, and the
    // corresponding encryption key and mac key
    //
    // For none primitives, both keys will be null
    // For verify mac primitives, the encryption key will be null
    // For homomorphic primitives, the encryption key will not contain
    // the space doing the operation
    public static class SecurityOperation {
        public final Key encryptionKey;
        public final Key macKey;
        public final SecurityPrimitive primitive;

        SecurityOperation(Key encryptionKey, Key macKey, SecurityPrimitive primitive) {
            this.encryptionKey = encryptionKey;
            this.macKey = macKey;
            this.primitive = primitive;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SecurityOperation that = (SecurityOperation) o;

            if (encryptionKey != null ? !encryptionKey.equals(that.encryptionKey) : that.encryptionKey != null)
                return false;
            if (macKey != null ? !macKey.equals(that.macKey) : that.macKey != null) return false;
            return primitive == that.primitive;
        }

        @Override
        public int hashCode() {
            int result = encryptionKey != null ? encryptionKey.hashCode() : 0;
            result = 31 * result + (macKey != null ? macKey.hashCode() : 0);
            result = 31 * result + primitive.hashCode();
            return result;
        }
    }

    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private boolean progress;

    // for each field, what space can encrypt data to it
    // (over the choices of paths in the program)
    private final Map<ModelField, Set<Space>> encryptionWriters = new HashMap<>();
    // for each field, what space can mac data on it
    // the first set is over the choices of paths in the program, the
    // second is the set of macs that need to be applied in that
    // program path (because multiple macs can be needed on a given program
    // path)
    private final Map<ModelField, Set<Set<Space>>> macWriters = new HashMap<>();

    // for each field, for each space, what security primitives it needs to apply
    // on a given field
    private final Map<ModelField, Map<Space, SecurityPrimitive>> securityPrimitives = new HashMap<>();

    // for each field, which encryption keys are needed
    private final Map<ModelField, Set<Key>> encryptionKeys = new HashMap<>();
    // for each field, which mac keys are needed
    private final Map<ModelField, Set<Key>> macKeys = new HashMap<>();

    public SecurityAnalysis(RavelCompiler driver, RavelApplication app, boolean debug) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;
    }

    public void run() {
        do {
            progress = false;
            runOneStep();
        } while(progress);
    }

    private void runOneStep() {
        for (Model m : app.getModels()) {
            for (ModelField field : m.getFields()) {
                // first compute who can encrypt it
                for (Space writer : field.getWriters()) {
                    field.forEachOperation((space, creator, op) -> {

                    });
                }

                // second compute who can mac it

                // third compute who needs to decrypt it, and who needs to mac it


            }
        }
    }
}
