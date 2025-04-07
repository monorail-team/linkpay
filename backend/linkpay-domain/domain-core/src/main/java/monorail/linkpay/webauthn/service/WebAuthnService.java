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

        if (authData.length < 37) { // 최소 길이 체크: 32(RP ID 해시)+1(플래그)+4(서명 카운트)
            throw new IllegalArgumentException("authData 길이가 너무 짧습니다.");
        }

        int flags = authData[32] & 0xFF;
        // AT(Attested Credential Data) 플래그가 설정되어 있는지 확인 (0x40)
        boolean hasAttestedCredentialData = (flags & 0x40) != 0;
        if (!hasAttestedCredentialData) {
            throw new LinkPayException(ExceptionCode.INVALID_REQUEST, "Attested Credential Data가 authData에 존재하지 않습니다.");
        }

        int offset = 37;
        // AAGUID: 16바이트
        offset += 16;

        // Credential ID 길이: 2바이트 (빅 엔디언)
        int credIdLen = ((authData[offset] & 0xFF) << 8) | (authData[offset + 1] & 0xFF);
        offset += 2;

        // Credential ID: credIdLen 바이트 (이미 등록 요청으로 전달된 credentialId와 일치하는지 확인할 수 있음)
        byte[] credentialIdBytes = Arrays.copyOfRange(authData, offset, offset + credIdLen);
        offset += credIdLen;

        // 나머지 바이트가 credentialPublicKey (COSE 형식의 CBOR 데이터)
        byte[] publicKeyBytes = Arrays.copyOfRange(authData, offset, authData.length);

        //추출한 credentialPublicKey 전체를 base64로 인코딩하여 저장
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);

        // 저장
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
            //credentialId  정보 조회
            WebAuthnCredential credential = credentialFetcher.fetchByMemberId(memberId);
            if (credential == null) {
                //등록되지 않은 사용자
                throw new RuntimeException("credential not found.");
            }

            // 2. credentialId 검증
            if (!credential.getCredentialId().equals(credentialId)) {
                //이거 뭐던져야 하지
                throw new RuntimeException("credential id mismatch.");
            }

            // 3. 클라이언트 데이터 파싱
            byte[] clientDataJSONBytes = Base64.getDecoder().decode(clientDataJSON);
            String clientDataJSONString = new String(clientDataJSONBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> clientData = mapper.readValue(clientDataJSONString, Map.class);

            Optional<AuthnChallenge> authnChallengeOpt = authnChallengeRepository.findByMemberId(memberId);
            // 4. 챌린지 검증
            String challengeFromClient = (String) clientData.get("challenge");
            if(authnChallengeOpt.isPresent()) {
                String storedChallenge = authnChallengeOpt.get().getChallenge();

                if (storedChallenge.isEmpty() || !challengeFromClient.equals(storedChallenge)) {
                    throw new RuntimeException("challenge mismatch.");
                }
                authnChallengeRepository.delete(authnChallengeOpt.get());
            }



            // 5. 인증 데이터 검증
            byte[] authDataBytes = Base64.getDecoder().decode(authenticatorData);
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            log.info("signatureBytes: {}", signatureBytes.toString());
            // 6. 공개키 복원
            byte[] publicKeyBytes = Base64.getDecoder().decode(credential.getPublicKey());

            // COSE 형식의 공개키를 Java 공개키 형태로 변환
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


            // 서명 알고리즘 결정 (COSE 키 타입에 따라 달라짐)
            String algorithm = determineAlgorithmFromCOSEKey(coseKey);

            Signature signatureVerifier = Signature.getInstance(algorithm);
            signatureVerifier.initVerify(publicKey);
            signatureVerifier.update(signedData);

            log.info("AuthenticatorData: {}", Arrays.toString(authDataBytes));
            log.info("ClientDataHash: {}", Arrays.toString(clientDataHash));
            log.info("SignedData: {}", Arrays.toString(signedData));

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
        // COSE 키 타입 확인
        int kty = ((Number) coseKey.get("1")).intValue(); // 1: kty (Key Type)

        if (kty == 2) { // 2: EC2 (Elliptic Curve)
            // 타원 곡선 파라미터 추출
            int crv = ((Number) coseKey.get("-1")).intValue(); // -1: crv (Curve)
            byte[] x = (byte[]) coseKey.get("-2"); // -2: x-coordinate
            byte[] y = (byte[]) coseKey.get("-3"); // -3: y-coordinate

            String curveName;
            switch (crv) {
                case 1:
                    curveName = "secp256r1"; // P-256
                    break;
                case 2:
                    curveName = "secp384r1"; // P-384
                    break;
                case 3:
                    curveName = "secp521r1"; // P-521
                    break;
                default:
                    throw new IllegalArgumentException("지원되지 않는 곡선 타입: " + crv);
            }

            // 타원 곡선 공개키 생성
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
            parameters.init(new ECGenParameterSpec(curveName));
            ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

            ECPoint point = new ECPoint(new BigInteger(1, x), new BigInteger(1, y));
            ECPublicKeySpec keySpec = new ECPublicKeySpec(point, ecParameters);

            KeyFactory keyFactory = KeyFactory.getInstance("EC");


            return keyFactory.generatePublic(keySpec);
        } else if (kty == 3) { // 3: RSA
            // RSA 키 파라미터 추출
            byte[] n = (byte[]) coseKey.get("-1"); // -1: n (Modulus)
            byte[] e = (byte[]) coseKey.get("-2"); // -2: e (Exponent)

            // RSA 공개키 생성
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, n), new BigInteger(1, e));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } else {
            throw new IllegalArgumentException("지원되지 않는 키 타입: " + kty);
        }
    }

    // COSE 키에 따른 서명 알고리즘 결정
    private String determineAlgorithmFromCOSEKey(Map<?, Object> coseKey) {
        int kty = ((Number) coseKey.get("1")).intValue(); // 1: kty (Key Type)

        if (kty == 2) { // 2: EC2 (Elliptic Curve)
            int alg = ((Number) coseKey.get("3")).intValue(); // 3: alg (Algorithm)
            switch (alg) {
                case -7:  // ES256
                    return "SHA256withECDSA";
                case -35: // ES384
                    return "SHA384withECDSA";
                case -36: // ES512
                    return "SHA512withECDSA";
                default:
                    return "SHA256withECDSA"; // 기본값
            }
        } else if (kty == 3) { // 3: RSA
            return "SHA256withRSA";
        } else {
            return "SHA256withECDSA"; // 기본값
        }
    }
}
