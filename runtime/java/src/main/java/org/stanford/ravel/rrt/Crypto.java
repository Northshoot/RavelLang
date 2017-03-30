package org.stanford.ravel.rrt;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by gcampagn on 2/28/17.
 */
public class Crypto {
    private static final SecurityMechanism SECURITY_MECHANISM = new SecurityMechanism();

    private final static String TAG =Crypto.class.getSimpleName();
    public static void apply_mac(byte[] data, int endofdata, int writeOffset, Key key) {
        try {
            Mac macAlgorithm = Mac.getInstance(SecurityMechanism.MAC_ALGORITHM);
            macAlgorithm.init(key);
            macAlgorithm.update(data, 0, endofdata);

            byte[] mac = macAlgorithm.doFinal();
            System.arraycopy(mac, 0, data, writeOffset, SECURITY_MECHANISM.getMACSize());
        } catch(NoSuchAlgorithmException |InvalidKeyException e) {
            throw new SecurityException("Failed to apply MAC", e);
        }
    }
    public static void verify_mac(byte[] data, int endofdata, int macoffset, Key key) {
        try {
            Mac macAlgorithm = Mac.getInstance(SecurityMechanism.MAC_ALGORITHM);
            macAlgorithm.init(key);
            macAlgorithm.update(data, 0, endofdata);

            byte[] mac = macAlgorithm.doFinal();
            boolean valid = true;
            for (int i = 0; i < SECURITY_MECHANISM.getMACSize(); i++) {
                if (mac[i] != data[macoffset + i])
                    valid = false;
            }
            if (!valid) {
                System.err.println("RRT Crypto Invalid MAC " );
                throw new SecurityException("Invalid MAC");
            }
        } catch(NoSuchAlgorithmException|InvalidKeyException e) {
            throw new SecurityException("Failed to verify MAC", e);
        }
    }

    public static void encrypt(byte[] data, int offset, int length, Key key) {
        try {
            // double the size of the IV to add space for incrementing the counter
            int ivSize = SECURITY_MECHANISM.getEncryptionIVSize();
            byte[] iv = new byte[ivSize * 2];
            System.arraycopy(data, offset, iv, 0, ivSize);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(ivParameterSpec);

            Cipher cipher = Cipher.getInstance(SecurityMechanism.ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);

            int pos = offset + ivSize;
            byte[] encrypted = cipher.update(data, pos, length - ivSize);
            assert encrypted.length == length - ivSize;
            System.arraycopy(encrypted, 0, data, pos, length - ivSize);

            encrypted = cipher.doFinal();
            assert encrypted.length == 0;
        } catch(NoSuchAlgorithmException|InvalidKeyException|NoSuchPaddingException |InvalidAlgorithmParameterException |IllegalBlockSizeException |BadPaddingException |InvalidParameterSpecException e) {
            throw new SecurityException("Failed to encrypt", e);
        }
    }

    public static void decrypt(byte[] data, int offset, int length, Key key) {
        try {
            // double the size of the IV to add space for incrementing the counter
            int ivSize = SECURITY_MECHANISM.getEncryptionIVSize();
            byte[] iv = new byte[ivSize * 2];
            System.arraycopy(data, offset, iv, 0, ivSize);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(ivParameterSpec);

            Cipher cipher = Cipher.getInstance(SecurityMechanism.ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, parameters);

            int pos = offset + ivSize;
            byte[] decrypted = cipher.update(data, pos, length - ivSize);
            assert decrypted.length == length - ivSize;

            // overwrite the iv with the decrypted data
            System.arraycopy(decrypted, 0, data, offset, length - ivSize);

            decrypted = cipher.doFinal();
            assert decrypted.length == 0;
        } catch(NoSuchAlgorithmException|InvalidKeyException|NoSuchPaddingException|InvalidAlgorithmParameterException|IllegalBlockSizeException|BadPaddingException|InvalidParameterSpecException e) {
            throw new SecurityException("Failed to decrypt", e);
        }
    }
}
