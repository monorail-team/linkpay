package monorail.linkpay.webauthn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.payment.service.PaymentTokenProvider;
import monorail.linkpay.webauthn.domain.AuthnChallenge;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.dto.WebAuthnChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnResponse;
import monorail.linkpay.webauthn.repository.AuthnChallengeRepository;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebAuthnService {

    private final static SecureRandom secureRandom = new SecureRandom();

    private final WebAuthnCredentialRepository credentialRepository;
    private final WebAuthnCredentialFetcher credentialFetcher;
    private final PaymentTokenProvider paymentTokenProvider;
    private final AuthnChallengeRepository authnChallengeRepository;


    public String getRegisterChallenge() {
        return generateRandomChallenge();
    }

    @Transactional
    public void registerAuthenticator(Long memberId, String credentialId, String attestationObject) {
        byte[] attestationBytes = Base64.getDecoder().decode(attestationObject);
        CBORFactory cborFactory = new CBORFactory();
        ObjectMapper cborMapper = new ObjectMapper(cborFactory);

        Map<String, Object> attestationMap = null;
        try {
            attestationMap = cborMapper.readValue(attestationBytes, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (attestationMap.containsKey("fmt")) {

        }

        String fmt = Optional.ofNullable(attestationMap.get("fmt"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(() -> new LinkPayException(ExceptionCode.INVALID_REQUEST, "지원하지 않는 인증 방식"));

        byte[] authData = (byte[]) attestationMap.get("authData");

        if (authData.length < 37) {
            throw new IllegalArgumentException("authData 길이가 너무 짧습니다.");
        }

        int flags = authData[32] & 0xFF;
        boolean hasAttestedCredentialData = (flags & 0x40) != 0;
        if (!hasAttestedCredentialData) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, "Attested Credential Data가 authData에 존재하지 않습니다.");
        }

        int offset = 37;
        offset += 16;
        int credIdLen = ((authData[offset] & 0xFF) << 8) | (authData[offset + 1] & 0xFF);
        offset += 2;
        byte[] credentialIdBytes = Arrays.copyOfRange(authData, offset, offset + credIdLen);
        offset += credIdLen;

        byte[] publicKeyBytes = Arrays.copyOfRange(authData, offset, authData.length);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        credentialRepository.save(WebAuthnCredential.builder()
                .credentialId(credentialId)
                .memberId(memberId)
                .publicKey(publicKeyBase64)
                .build());
    }

    @Transactional
    public WebAuthnChallengeResponse getAuthChallenge(Long memberId) {
        WebAuthnCredential credential = credentialFetcher.fetchByMemberId(memberId);
        String challenge = generateRandomChallenge();

        authnChallengeRepository.save(AuthnChallenge.builder()
                .memberId(memberId)
                .challenge(challenge)
                .build());

        return new WebAuthnChallengeResponse(challenge, credential.getCredentialId());
    }

    @Transactional
    public WebAuthnResponse verifyAuthentication(Long memberId, String credentialId, String clientDataJSON, String authenticatorData, String signature) {
        try {
            WebAuthnCredential credential = credentialFetcher.fetchByMemberId(memberId);
            if (credential == null) {
                throw new RuntimeException("credential not found.");
            }


            if (!credential.getCredentialId().equals(credentialId)) {

                throw new RuntimeException("credential id mismatch.");
            }


            byte[] clientDataJSONBytes = Base64.getDecoder().decode(clientDataJSON);
            String clientDataJSONString = new String(clientDataJSONBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> clientData = mapper.readValue(clientDataJSONString, Map.class);

            Optional<AuthnChallenge> authnChallengeOpt = authnChallengeRepository.findByMemberId(memberId);

            String challengeFromClient = (String) clientData.get("challenge");
            if(authnChallengeOpt.isPresent()) {
                String storedChallenge = authnChallengeOpt.get().getChallenge();

                if (storedChallenge.isEmpty() || !challengeFromClient.equals(storedChallenge)) {
                    throw new RuntimeException("challenge mismatch.");
                }
                authnChallengeRepository.delete(authnChallengeOpt.get());
            }




            byte[] authDataBytes = Base64.getDecoder().decode(authenticatorData);
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            byte[] publicKeyBytes = Base64.getDecoder().decode(credential.getPublicKey());


            CBORFactory cborFactory = new CBORFactory();
            ObjectMapper cborMapper = new ObjectMapper(cborFactory);
            @SuppressWarnings("unchecked")
            Map<?, Object> coseKey = cborMapper.readValue(publicKeyBytes, Map.class);

            log.info("COSE Key Map: {}", coseKey);
            PublicKey publicKey = convertCOSEKeyToPublicKey(coseKey);


            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] clientDataHash = digest.digest(clientDataJSONBytes);
            byte[] signedData = new byte[authDataBytes.length + clientDataHash.length];
            System.arraycopy(authDataBytes, 0, signedData, 0, authDataBytes.length);
            System.arraycopy(clientDataHash, 0, signedData, authDataBytes.length, clientDataHash.length);



            String algorithm = determineAlgorithmFromCOSEKey(coseKey);

            Signature signatureVerifier = Signature.getInstance(algorithm);
            signatureVerifier.initVerify(publicKey);
            signatureVerifier.update(signedData);

            if(signatureVerifier.verify(signatureBytes)){
                return new WebAuthnResponse(paymentTokenProvider.generateFor(memberId));
            }else{
                throw new RuntimeException("signature verification failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String generateRandomChallenge() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private PublicKey convertCOSEKeyToPublicKey(Map<?, Object> coseKey) throws Exception {
        log.info("COSE Key Map: {}", coseKey);
        for (Object key : coseKey.keySet()) {
            log.info("COSE Key: {} | Type: {}", key, key.getClass().getName());
        }

        int kty = ((Number) coseKey.get("1")).intValue();

        if (kty == 2) {
            int crv = ((Number) coseKey.get("-1")).intValue();
            byte[] x = (byte[]) coseKey.get("-2");
            byte[] y = (byte[]) coseKey.get("-3");

            String curveName;
            switch (crv) {
                case 1:
                    curveName = "secp256r1";
                    break;
                case 2:
                    curveName = "secp384r1";
                    break;
                case 3:
                    curveName = "secp521r1";
                    break;
                default:
                    throw new IllegalArgumentException("지원되지 않는 곡선 타입: " + crv);
            }

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
            parameters.init(new ECGenParameterSpec(curveName));
            ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

            ECPoint point = new ECPoint(new BigInteger(1, x), new BigInteger(1, y));
            ECPublicKeySpec keySpec = new ECPublicKeySpec(point, ecParameters);

            KeyFactory keyFactory = KeyFactory.getInstance("EC");


            return keyFactory.generatePublic(keySpec);
        } else if (kty == 3) {
            byte[] n = (byte[]) coseKey.get("-1");
            byte[] e = (byte[]) coseKey.get("-2");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, n), new BigInteger(1, e));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } else {
            throw new IllegalArgumentException("지원되지 않는 키 타입: " + kty);
        }
    }

    private String determineAlgorithmFromCOSEKey(Map<?, Object> coseKey) {
        int kty = ((Number) coseKey.get("1")).intValue();

        if (kty == 2) {
            int alg = ((Number) coseKey.get("3")).intValue();
            switch (alg) {
                case -7:
                    return "SHA256withECDSA";
                case -35:
                    return "SHA384withECDSA";
                case -36:
                    return "SHA512withECDSA";
                default:
                    return "SHA256withECDSA";
            }
        } else if (kty == 3) {
            return "SHA256withRSA";
        } else {
            return "SHA256withECDSA";
        }
    }
}
