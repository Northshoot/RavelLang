package org.stanford.ravel.rrt.lang;

import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.SecurityMechanism;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;
import org.stanford.ravel.rrt.utils.ByteWork;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Date;

/**
 * Implementations for TIntrinsic calls
 *
 * Created by gcampagn on 2/21/17.
 */
public class Intrinsic {
    private static final SecurityMechanism SECURITY_MECHANISM = new SecurityMechanism();

    public static String endpoint_get_name(Endpoint ep) {
        return ep.getName();
    }

    public static int array_length(Object[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(byte[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(int[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(double[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    public static int array_length(boolean[] array) {
        if (array == null)
            return 0;
        return array.length;
    }

    public static int strlen(String str) {
        if (str == null)
            return 0;
        return str.length();
    }

    public static boolean extract_bool(byte[] data, int pos) {
        return ByteWork.convertOneByteToBool(ByteWork.getBytes(data, pos, pos+1));
    }

    public static byte extract_byte(byte[] data, int pos) {
        return data[pos];
    }

    public static int extract_uint16(byte[] data, int pos) {
        return ByteWork.convertTwoUnsignedBytesToInt(ByteWork.getBytes(data, pos, pos+2));
    }

    public static int extract_int32(byte[] data, int pos) {
        return ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, pos, pos+4));
    }

    public static Error extract_error_msg(byte[] data, int pos) {
        return ByteWork.convertFourBytesToError(ByteWork.getBytes(data, pos, pos+4));
    }

    public static Date extract_timestamp(byte[] data, int pos) {
        return ByteWork.convertFourBytesToDate(ByteWork.getBytes(data, pos, pos+4));
    }

    public static String extract_str(byte[] data, int pos, int size) {
        return ByteWork.convertBytesToString(ByteWork.getBytes(data, pos, pos+size));
    }

    public static void write_int32(byte[] data, int pos, int value) {
        byte[] bytes = ByteWork.getByteArray(value);
        System.arraycopy(bytes, 0, data, pos, bytes.length);
    }

    public static void apply_mac(byte[] data, int endofdata, int writeOffset, Key key) {
        try {
            Mac macAlgorithm = Mac.getInstance(SecurityMechanism.MAC_ALGORITHM);
            macAlgorithm.init(key);
            macAlgorithm.update(data, 0, endofdata);

            byte[] mac = macAlgorithm.doFinal();
            System.arraycopy(mac, 0, data, writeOffset, SECURITY_MECHANISM.getMACSize());
        } catch(NoSuchAlgorithmException|InvalidKeyException e) {
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
            if (!valid)
                throw new SecurityException("Invalid MAC");
        } catch(NoSuchAlgorithmException|InvalidKeyException e) {
            throw new SecurityException("Failed to verify MAC", e);
        }
    }

    public static void encrypt(byte[] data, int offset, int length, Key key) {
        try {
            int ivSize = SECURITY_MECHANISM.getEncryptionIVSize();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(data, offset, ivSize);
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
        } catch(NoSuchAlgorithmException|InvalidKeyException|NoSuchPaddingException|InvalidAlgorithmParameterException|IllegalBlockSizeException|BadPaddingException|InvalidParameterSpecException e) {
            throw new SecurityException("Failed to encrypt", e);
        }
    }

    public static void decrypt(byte[] data, int offset, int length, Key key) {
        try {
            int ivSize = SECURITY_MECHANISM.getEncryptionIVSize();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(data, offset, ivSize);
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

    public static Key load_key(DispatcherAPI dispatcher, int keyId) {
        Key key = dispatcher.getKeyProvider().loadKey(keyId);
        if (key == null)
            throw new SecurityException("Invalid key identifier " + keyId);
        return key;
    }

    public static void array_copy(byte[] tgt, byte[] source, int tgtOffset, int srcOffset, int size) {
        System.arraycopy(source, srcOffset, tgt, tgtOffset, size);
    }

    private static final SecureRandom randomSource = new SecureRandom();
    public static void array_fill_random(byte[] buffer, int offset, int length) {
        byte[] random = new byte[length];
        randomSource.nextBytes(random);
        System.arraycopy(random, 0, buffer, offset, length);
    }
}
