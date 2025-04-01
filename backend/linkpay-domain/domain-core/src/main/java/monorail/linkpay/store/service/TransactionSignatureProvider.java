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
public class TransactionSignatureProvider {

    // todo 외부로부터 설정 정보 주입
    private final String signatureAlgorithm = "SHA256withRSA";
    private final String keyAlgorithm = "RSA";

    public String createSignature(final Object data, final String encryptKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = KeyFactory.getInstance(keyAlgorithm).generatePrivate(spec);

            // todo 해시 적용 후 암호화 해야함
            Signature sig = Signature.getInstance(signatureAlgorithm);
            sig.initSign(privateKey);
            sig.update(JsonUtil.toJson(data).value().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            LinkPayException linkPayException = new LinkPayException(ExceptionCode.INVALID_REQUEST, "서명 생성 중 예외 발생");
            linkPayException.initCause(e);
            throw linkPayException;
        }
    }

    public void verifySignature(final Object data, final String signature, final String decryptKey) {
        boolean verified = verify(data, signature, decryptKey);
        if(!verified) {
            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "유효하지 않은 서명");
        }
    }

    private boolean verify(final Object data, final String signature, final String decryptKey){
        try {
            byte[] keyBytes = Base64.getDecoder().decode(decryptKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(spec);

            Signature sig = Signature.getInstance(signatureAlgorithm);
            sig.initVerify(publicKey);
            sig.update(JsonUtil.toJson(data).value().getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            LinkPayException linkPayException = new LinkPayException(ExceptionCode.INVALID_REQUEST, "서명 검증 중 예외 발생");
            linkPayException.initCause(e);
            throw linkPayException;
        }
    }
}
