package monorail.linkpay.auth.service;

import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.json.Json;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class TransactionSignatureProvider {

    // todo 외부로부터 설정 정보 주입
    private final String signatureAlgorithm = "SHA256withRSA";
    private final String keyAlgorithm = "RSA";

    public String createSignature(final Json data, final String encryptKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = KeyFactory.getInstance(keyAlgorithm).generatePrivate(spec);

            Signature sig = Signature.getInstance(signatureAlgorithm);
            sig.initSign(privateKey);
            sig.update(data.value().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, e.getMessage());
        }
    }

    public boolean verifySignature(final Json data, final String signature, final String decryptKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(decryptKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(spec);

            Signature sig = Signature.getInstance(signatureAlgorithm);
            sig.initVerify(publicKey);
            sig.update(data.value().getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, e.getMessage());
        }
    }
}
