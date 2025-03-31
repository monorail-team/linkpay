package monorail.linkpay.util.key;

import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyPairUtil {

    private static final int KEY_SIZE = 2048;

    public static KeyPair generateKeyPair(KeyAlgorithm keyAlgorithm) {
        return generateKeyPair(KEY_SIZE, keyAlgorithm);
    }

    public static KeyPair generateKeyPair(int keySize, KeyAlgorithm keyAlgorithm) {
        try {
            java.security.KeyPairGenerator generator = java.security.KeyPairGenerator.getInstance(keyAlgorithm.getValue());
            generator.initialize(keySize, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, "지원하지 않는 키 생성 알고리즘: " + keyAlgorithm.getValue());
        }
    }
}
