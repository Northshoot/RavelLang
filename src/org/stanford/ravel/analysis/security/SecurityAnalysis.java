package org.stanford.ravel.analysis.security;

import org.stanford.ravel.RavelApplication;
import org.stanford.ravel.RavelCompiler;
import org.stanford.ravel.analysis.Operation;
import org.stanford.ravel.compiler.ir.typed.TBlock;
import org.stanford.ravel.compiler.ir.typed.TFieldStore;
import org.stanford.ravel.compiler.ir.typed.TInstruction;
import org.stanford.ravel.primitives.*;

import java.util.*;

/**
 * Compute, for each space, what it needs to do security-wise for each field in each
 * model it instantiates (including which keys are each field encrypted with, which
 * keys they are mac'ed with etc).
 *
 * Created by gcampagn on 2/13/17.
 */
public class SecurityAnalysis {
    private final RavelCompiler driver;
    private final RavelApplication app;
    private final boolean debug;
    private boolean progress;

    // for each field, what space can encrypt data to it
    // (over the choices of paths in the program)
    private final Map<ModelField, Set<Space>> encryptionWriters = new HashMap<>();
    // for each field, what space can mac data on it
    // (over the choices of paths in the program)
    private final Map<ModelField, Set<Space>> macWriters = new HashMap<>();

    // for each field, for each space, what security primitives it needs to apply
    // on a given field
    private final Map<ModelField, Map<Space, SecurityLevel>> securityPrimitives = new HashMap<>();

    // for each field, which encryption keys are needed
    private final Map<ModelField, Set<Key>> encryptionKeys = new HashMap<>();
    // and for each key, which fields it applies to
    // (this ensures deduplication of keys)
    //
    // NOTE: ModelField does not override equals and hashcode, but this is ok
    // because we never create ModelFields after we're done with compilation
    private final Map<Key, Set<ModelField>> encryptedFields = new HashMap<>();

    // for each field, which mac keys are needed
    private final Map<ModelField, Set<Key>> macKeys = new HashMap<>();
    // and for each key, which fields are covered by its mac
    private final Map<Key, Set<ModelField>> macedFields = new HashMap<>();

    // for each field on each flow, the homomorphic operation that will be effectively applied
    private final Map<ModelField, Map<Flow, HomomorphicOperation>> homomorphicOperations = new HashMap<>();

    // the set of all keys (which is used to assign short key ids)
    private final Map<Key, Integer> allKeys = new HashMap<>();

    private final boolean recordLevelEncryption;
    private final boolean enableHomomorphic;

    public SecurityAnalysis(RavelCompiler driver, RavelApplication app, boolean debug, boolean recordLevelEncryption, boolean enableHomomorphic) {
        this.driver = driver;
        this.app = app;
        this.debug = debug;

        if (recordLevelEncryption && enableHomomorphic) {
            driver.emitWarning(null, "homomorphic encryption cannot be applied at the record level; falling back to non-homomorphic");
            enableHomomorphic = false;
        }

        this.recordLevelEncryption = recordLevelEncryption;
        this.enableHomomorphic = enableHomomorphic;
    }

    public void run() {
        do {
            progress = false;
            computeEncryptionMacWriters();
        } while(progress);

        computeSecurityLevels();
        computeKeys();

        if (debug) {
            System.out.println("Security Analysis");
            dumpSecurityAnalysis();
        }

        System.out.println("Keys");
        for (Key key : allKeys.keySet()) {
            System.out.println(key);
        }
    }

    private void dumpSecurityAnalysis() {
        for (Model m : app.getModels()) {
            for (ModelField field : m.getFields()) {
                System.out.println(field + ":");
                System.out.println("encrypted by " + encryptionWriters.getOrDefault(field, Collections.emptySet()));
                System.out.println("mac-ed by " + macWriters.getOrDefault(field, Collections.emptySet()));
                System.out.println("security primitives: " + securityPrimitives.getOrDefault(field, Collections.emptyMap()));
                System.out.println("encryption keys: " + encryptionKeys.getOrDefault(field, Collections.emptySet()));
                System.out.println("mac keys: " + macKeys.getOrDefault(field, Collections.emptySet()));
            }
        }
    }

    private void addSpaceToEncryption(ModelField field, Space space) {
        progress = encryptionWriters.computeIfAbsent(field, (unused) -> new HashSet<>()).add(space) || progress;
    }
    private void addSpaceToMac(ModelField field, Space space) {
        progress = macWriters.computeIfAbsent(field, (unused) -> new HashSet<>()).add(space) || progress;
    }

    private static boolean isFieldWrite(TFieldStore instr, ModelField field) {
        return instr.compoundType.equalsExceptQualifiers(field.getModel().getType().getRecordType())
                && instr.field.equals(field.getName());
    }

    private static boolean writesToField(Space space, ModelField field) {
        for (ConcreteController ic : space.getControllers()) {
            for (LinkedEvent event : ic) {
                for (TBlock block : event.getHandler().getBody().getControlFlowGraph()) {
                    for (TInstruction instr : block) {
                        if (instr instanceof TFieldStore) {
                            TFieldStore fieldStore = (TFieldStore)instr;
                            if (isFieldWrite(fieldStore, field))
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private void computeEncryptionMacWriters() {
        for (Model m : app.getModels()) {
            // we don't do any security primitive on local models
            if (m.getModelType() == Model.Type.LOCAL)
                continue;

            for (Space space : app.getSpaces()) {
                boolean anyEncrypt = false;
                boolean anyMAC = false;

                for (ModelField field : m.getFields()) {
                    if (!writesToField(space, field))
                        continue;

                    boolean encrypt = false;
                    boolean mac = false;
                    // check if this write is actually plaintext and not just
                    // encrypted data
                    for (Map.Entry<Space, Operation> entry : field.getOperations(space).entrySet()) {
                        Space creator = entry.getKey();
                        Operation op = entry.getValue();

                        if (creator == space) {
                            // no matter what we do, if we create the value we are dealing with
                            // plaintext
                            encrypt = true;
                            mac = true;
                        } else if (op == Operation.ANY) {
                            // no matter who created, if we're decrypting the remote value, we have
                            // to reencrypt it
                            encrypt = true;
                            mac = true;
                        } else if (op != Operation.NONE && op != Operation.MOVE) {
                            if (enableHomomorphic) {
                                // if we're computing on encrypted data, we only need to mac it again
                                mac = true;
                            } else {
                                encrypt = true;
                                mac = true;
                            }
                        }
                    }

                    // if we encrypt, then we must mac as well
                    assert !encrypt || mac;

                    if (encrypt)
                        addSpaceToEncryption(field, space);
                    if (mac)
                        addSpaceToMac(field, space);

                    anyEncrypt = encrypt;
                    anyMAC = mac;
                }

                // if we encrypt, then we must mac as well
                assert !anyEncrypt || anyMAC;

                if (recordLevelEncryption) {
                    // if we're doing record level encryption, the moment we encrypt anything we encrypt
                    // everything
                    // (and same for mac)

                    for (ModelField field : m.getFields()) {
                        if (anyEncrypt)
                            addSpaceToEncryption(field, space);
                        if (anyMAC)
                            addSpaceToMac(field, space);
                    }
                }
            }
        }
    }

    private SecurityLevel securityLevelForField(ModelField field, Space space) {
        Map<Space, Operation> operations = field.getOperations(space);
        int nremotewriters = 0;
        for (Space writer : encryptionWriters.getOrDefault(field, Collections.emptySet())) {
            if (writer == space)
                continue;
            nremotewriters++;
        }

        SecurityLevel prim = SecurityLevel.NONE;
        for (Map.Entry<Space, Operation> entry : operations.entrySet()) {
            Space creator = entry.getKey();
            Operation op = entry.getValue();

            if (creator == space) {
                // no matter what we do, if we create the value we are dealing with
                // plaintext and we have nothing to do
                prim = SecurityLevel.meet(prim, SecurityLevel.NONE);
            } else {
                switch (op) {
                    case NONE:
                        // if we do nothing, we do nothing
                        prim = SecurityLevel.meet(prim, SecurityLevel.NONE);
                        break;
                    case MOVE:
                        // if we move the value around (and the move was not copy-propagated
                        // or dead-code eliminated) that means we're changing model or
                        // saving locally, which means we should at least verify the mac
                        prim = SecurityLevel.meet(prim, SecurityLevel.VERIFY_MAC);
                        break;

                    case IADD:
                        if (enableHomomorphic) {
                            // if one space makes all the writes, it's homomorphic addition
                            // if multiple spaces make the writes, it's multiparty addition
                            if (nremotewriters <= 1)
                                prim = SecurityLevel.meet(prim, SecurityLevel.HOMO_ADD);
                            else
                                prim = SecurityLevel.meet(prim, SecurityLevel.MULTIPARTY_ADD);
                        } else {
                            prim = SecurityLevel.meet(prim, SecurityLevel.DECRYPT);
                        }
                        break;

                    case IMUL:
                        if (enableHomomorphic) {
                            if (nremotewriters <= 1)
                                prim = SecurityLevel.meet(prim, SecurityLevel.HOMO_MUL);
                            else // we don't have multiparty multiplication
                                prim = SecurityLevel.meet(prim, SecurityLevel.DECRYPT);
                        } else {
                            prim = SecurityLevel.meet(prim, SecurityLevel.DECRYPT);
                        }

                        // we don't deal with these for now...
                    case CONCAT:
                    case INDEX_LOAD:
                    case INDEX_STORE:
                    case ANY:
                        prim = SecurityLevel.meet(prim, SecurityLevel.DECRYPT);
                        break;
                }
            }
        }

        return prim;
    }

    private void computeSecurityLevels() {
        for (Model m : app.getModels()) {
            // we don't do any security primitive on local models
            if (m.getModelType() == Model.Type.LOCAL)
                continue;

            for (Space space : app.getSpaces()) {
                SecurityLevel global = SecurityLevel.NONE;

                for (ModelField field : m.getFields()) {
                    SecurityLevel prim = securityLevelForField(field, space);
                    global = SecurityLevel.meet(prim, global);

                    if (prim != SecurityLevel.NONE)
                        securityPrimitives.computeIfAbsent(field, (unused) -> new HashMap<>()).put(space, prim);
                }

                if (recordLevelEncryption) {
                    // overwrite the security level of all fields in the model if we're
                    // asked to do record level encryption
                    for (ModelField field : m.getFields()) {
                        if (global != SecurityLevel.NONE)
                            securityPrimitives.computeIfAbsent(field, (unused) -> new HashMap<>()).put(space, global);
                    }
                }
            }
        }
    }

    private void addEncryptionKeyForField(ModelField field, Key key) {
        encryptionKeys.computeIfAbsent(field, (unused) -> new HashSet<>()).add(key);
        encryptedFields.computeIfAbsent(key, (unused) -> new HashSet<>()).add(field);
    }

    private void addMacKeyForField(ModelField field, Key key) {
        macKeys.computeIfAbsent(field, (unused) -> new HashSet<>()).add(key);
        macedFields.computeIfAbsent(key, (unused) -> new HashSet<>()).add(field);
    }

    private Key keyGen(Collection<Space> owners, Object keyDiscriminator, KeyType keyType) {
        Key newKey = new Key(owners, keyDiscriminator, keyType);

        if (allKeys.containsKey(newKey)) {
            int oldKeyId = allKeys.get(newKey);
            newKey.setKeyId(oldKeyId);
        } else {
            newKey.setKeyId(allKeys.size());
            allKeys.put(newKey, newKey.getKeyId());
        }

        return newKey;
    }

    private void computeEncryptionForField(ModelField field, Flow flow) {
        Space encryptor = flow.getSource();
        if (!encryptionWriters.getOrDefault(field, Collections.emptySet()).contains(encryptor))
            return;

        // the source of this flow writes to this field

        // compute the overall homomorphic operation that will be performed on this
        // field on this flow
        HomomorphicOperation homoOp = HomomorphicOperation.NONE;

        for (Space space : flow) {
            if (space == flow.getSource())
                continue;

            SecurityLevel prim = securityPrimitives.getOrDefault(field, Collections.emptyMap())
                    .getOrDefault(space, SecurityLevel.NONE);
            homoOp = HomomorphicOperation.meet(homoOp, HomomorphicOperation.forPrimitive(prim));
        }
        if (!enableHomomorphic)
            homoOp = HomomorphicOperation.NONE;

        // now compute the type of keys that we need on this flow
        KeyType encryptionKey = null;
        KeyType decryptionKey = null;
        switch (homoOp) {
            case NONE:
                encryptionKey = decryptionKey = KeyType.SYMMETRIC_ENCRYPT;
                break;
            case HOMO_ADD:
                encryptionKey = KeyType.HOMO_ADD_PUBLIC;
                decryptionKey = KeyType.HOMO_ADD_SECRET;
                break;
            case HOMO_MUL:
                encryptionKey = KeyType.HOMO_MUL_PUBLIC;
                decryptionKey = KeyType.HOMO_MUL_SECRET;
                break;

            case MULTIPARTY_ADD:
                // multiparty addition uses a secret sharing mechanism which requires
                // no extra keys
                encryptionKey = decryptionKey = KeyType.SYMMETRIC_ENCRYPT;
                break;

            case INVALID:
                // uh oh! different spaces along this flow are trying to do different
                // homomorphic operations on the same thing
                // we could in principle do full homomorphic, but that's just crazy
                // so in this case we bail and decrypt everywhere
                //
                // this is sad, so we warn the user
                driver.emitWarning(null, "Cannot perform homomorphic operation on " + field + ": multiple spaces along the same flow perform incompatible operations");
                break;
        }

        // compute all the decryptors
        Set<Space> decryptors = new HashSet<>();

        for (Space space : flow) {
            if (space == flow.getSource())
                continue;

            SecurityLevel prim = securityPrimitives.getOrDefault(field, Collections.emptyMap())
                    .getOrDefault(space, SecurityLevel.NONE);

            switch (prim) {
                case NONE:
                case VERIFY_MAC:
                    break;

                case HOMO_ADD:
                case HOMO_MUL:
                case MULTIPARTY_ADD:
                    if (homoOp == HomomorphicOperation.INVALID)
                        decryptors.add(space);
                    break;
                case DECRYPT:
                    decryptors.add(space);
                    break;
            }
        }

        if (encryptionKey == decryptionKey) {
            // make a symmetric key
            decryptors.add(encryptor);
            Key key = keyGen(decryptors, encryptor.getName(), encryptionKey);

            addEncryptionKeyForField(field, key);
            encryptor.addSecurityOperation(field, flow.getNext(encryptor), flow, key, SecurityPrimitive.ENCRYPT, 0);
            for (Space decryptor : decryptors) {
                if (decryptor == encryptor)
                    continue;
                decryptor.addSecurityOperation(field, flow.getPrevious(decryptor), flow, key, SecurityPrimitive.DECRYPT, 0);
            }
        } else {
            // make (public, secret) key pair
            Key publicKey = keyGen(Collections.singleton(encryptor), encryptor.getName(), encryptionKey);
            Key secretKey = keyGen(decryptors, encryptor.getName(), decryptionKey);

            addEncryptionKeyForField(field, publicKey);
            addEncryptionKeyForField(field, secretKey);

            encryptor.addSecurityOperation(field, flow.getNext(encryptor), flow, publicKey, SecurityPrimitive.ENCRYPT, 0);
            for (Space decryptor : decryptors) {
                decryptor.addSecurityOperation(field, flow.getPrevious(decryptor), flow, secretKey, SecurityPrimitive.DECRYPT, 0);
            }
        }

        if (homoOp != HomomorphicOperation.NONE && homoOp != HomomorphicOperation.INVALID) {
            homomorphicOperations.computeIfAbsent(field, (unused) -> new HashMap<>()).put(flow, homoOp);
        }
    }

    private void computeMacForField(ModelField field, Flow flow) {
        // only the source of the flow can write to the model, so only
        // the source of the flow can be a mac writer
        //
        // this is true even with multi-step flows because those only exist
        // in streaming models, and streaming model records are immutable
        // this is true even with homeomorphic operations because the result
        // of those have to be stored in some other model, with some other
        // flow

        Space writer = flow.getSource();
        if (!macWriters.getOrDefault(field, Collections.emptySet()).contains(writer))
            return;

        boolean foundWriter = false;
        int offset = 0;
        // skip all spaces before writer in the flow, and then make a mac
        // for each of the subsequent spaces
        for (Space reader : flow) {
            if (reader == writer) {
                foundWriter = true;
                continue;
            }
            if (!foundWriter)
                continue;

            // check if reader needs to verify the mac
            SecurityLevel prim = securityPrimitives.getOrDefault(field, Collections.emptyMap())
                    .getOrDefault(reader, SecurityLevel.NONE);
            if (!prim.requiresVerifyMac())
                continue;

            Key key = keyGen(Arrays.asList(writer, reader), writer.getName() + "->" + reader.getName(), KeyType.SYMMETRIC_MAC);
            addMacKeyForField(field, key);

            writer.addSecurityOperation(field, flow.getNext(writer), flow, key, SecurityPrimitive.APPLY_MAC, offset);
            reader.addSecurityOperation(field, flow.getPrevious(reader), flow, key, SecurityPrimitive.VERIFY_MAC, offset);
            offset ++;
        }
        flow.setNumMACs(offset);

        // NOTE: TRICKY:
        //
        // we make the following assumptions:
        // for replicated models, all flows are single hop
        // for streaming models, there is only one flow
        //
        // these assumptions combined mean that the verifying code can figure out the position
        // of the mac based on just an offset
        // the offset is only meaningful within a flow, so the verifying code must retrieve
        // (implicitly) the flow that a transmission belongs to, given only the previous hop
    }

    private void computeKeys() {
        for (Model m : app.getModels()) {
            // Create a set of keys for each flow
            for (Flow flow : m.getFlows()) {
                // ignore local flows
                if (flow.getSource() == flow.getSink())
                    continue;

                assert m.getModelType() != Model.Type.LOCAL;

                // use different keys for different fields
                // (and let the hash table deduplicate)
                for (ModelField field : m.getFields()) {
                    computeEncryptionForField(field, flow);
                    computeMacForField(field, flow);
                }
            }
        }
    }
}
