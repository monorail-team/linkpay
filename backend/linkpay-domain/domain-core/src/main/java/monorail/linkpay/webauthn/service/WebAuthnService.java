package monorail.linkpay.webauthn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.payment.service.PaymentTokenProvider;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.dto.AuthChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnSuccessResponse;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
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
    public AuthChallengeResponse getAuthChallenge(Long memberId) {
        WebAuthnCredential credential = credentialFetcher.fetchByMemberId(memberId);
        String challenge = generateRandomChallenge();
        // TODO: 세션에 챌린지를 기록 & TTL
        return new AuthChallengeResponse(challenge, credential.getCredentialId());
    }

    @Transactional
    public WebAuthnSuccessResponse verifyAuthentication(Long memberId, String credentialId, String clientDataJSON, String authenticatorData) {
        WebAuthnCredential credential = credentialFetcher.fetchByCredentialId(credentialId);
        if (!Objects.equals(credential.getMemberId(), memberId)) {
            throw new LinkPayException(ExceptionCode.FORBIDDEN_ACCESS, "지문 인증 실패");
        }

        // TODO: 세션에서 챌린지 삭제?
        return new WebAuthnSuccessResponse(paymentTokenProvider.generateFor(memberId));
    }

    private String generateRandomChallenge() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
