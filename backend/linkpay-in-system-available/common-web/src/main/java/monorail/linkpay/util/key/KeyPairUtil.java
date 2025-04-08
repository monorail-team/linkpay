package monorail.linkpay.util.key;

import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.util.Objects.isNull;

public class KeyPairUtil {


    public static KeyPair generateKeyPair(final KeyAlgorithm keyAlgorithm) {
        try {
            KeyPairGenerator generator = java.security.KeyPairGenerator.getInstance(keyAlgorithm.getValue());
            if (!isNull(keyAlgorithm.getKeySize())) {
                generator.initialize(keyAlgorithm.getKeySize(), new SecureRandom());
            }
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, "지원하지 않는 키 생성 알고리즘: " + keyAlgorithm.getValue());
        }
    }
}
