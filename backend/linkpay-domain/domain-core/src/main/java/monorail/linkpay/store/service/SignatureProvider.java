package monorail.linkpay.store.service;

import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.json.JsonUtil;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SupportLayer
public class SignatureProvider {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String KEY_ALGORITHM = "RSA";

    public String sign(final Object data, final String base64PrivateKey) {
        try {
            PrivateKey privateKey = decodePrivateKey(base64PrivateKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(toBytes(data));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, "서명 생성 실패");
        }
    }

    public void verify(final Object data, final String base64Signature, final String base64PublicKey) {
        try {
            PublicKey publicKey = decodePublicKey(base64PublicKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(toBytes(data));
            byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);

            if (!signature.verify(signatureBytes)) {
                throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "유효하지 않은 서명");
            }
        } catch (Exception e) {
            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "서명 검증 실패");
        }
    }

    private byte[] toBytes(Object data) {
        return JsonUtil.toJson(data).value().getBytes(StandardCharsets.UTF_8);
    }

    private PrivateKey decodePrivateKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(spec);
    }

    private PublicKey decodePublicKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(spec);
    }
}
