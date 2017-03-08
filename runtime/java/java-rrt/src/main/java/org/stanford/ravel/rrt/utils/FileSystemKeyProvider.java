package org.stanford.ravel.rrt.utils;

import org.stanford.ravel.rrt.KeyProvider;

import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by gcampagn on 2/28/17.
 */
public class FileSystemKeyProvider implements KeyProvider {
    private final Map<Integer, Key> keys = new HashMap<>();

    public FileSystemKeyProvider() {}

    public void load(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split("\\s+");

            if (tokens.length != 3) {
                System.err.println("Badly formatted line " + line);
                continue;
            }

            int keyId = Integer.parseInt(tokens[0]);

            String keyType = tokens[1];
            switch (keyType) {
                case "AES":
                case "HMAC":
                    byte[] keyBytes = ByteWork.bytesFromHex(tokens[2]);
                    Key key = new SecretKeySpec(keyBytes, keyType);
                    keys.put(keyId, key);
                    break;

                default:
                    System.err.println("Unrecognized key type " + keyType);
            }
        }
    }

    public Key loadKey(int keyId) {
        return keys.get(keyId);
    }
}
