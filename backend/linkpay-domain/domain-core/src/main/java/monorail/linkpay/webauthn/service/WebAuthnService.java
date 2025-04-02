package monorail.linkpay.webauthn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WebAuthnService {

    private final WebAuthnCredentialRepository credentialRepository;
    private final WebAuthnCredentialFetcher credentialFetcher;

    /**
     * 아래 두 챌린지는 현재는 같은 역을을 합니다. 향후 확장성을 위해 분리 해놓았습니다.
     * ex) 인증 Challenge생성에서 다른 로직을 사용하거나 검증 절차를 거칠경우
     */
    public String generateAuthChallenge(Long memberId) {
        return generateRandomChallenge();
    }

    public String generateRegisterChallenge(Long memberId) {
        return generateRandomChallenge();
    }
    public boolean verifyAuthentication(Long memberId, String credentialId, String clientDataJSON, String authenticatorData) {
        Optional<WebAuthnCredential> optCredential = credentialFetcher.fetchByCredentialId(credentialId);
        if (optCredential.isEmpty()) {
            return false;
        }
        WebAuthnCredential credential = optCredential.get();
        return credential.getMemberId().equals(memberId);
    }

    public boolean isRegistered(Long memberId) {
        return credentialFetcher.fetchByMemberId(memberId).isPresent();
    }



    public boolean registerAuthenticator(Long memberId, String credentialId, String clientDataJSON, String attestationObject) {

//        String extractedPublicKey = "simulatedPublicKey";
//
//        WebAuthnCredential credential = WebAuthnCredential.builder()
//                .credentialId(credentialId)
//                .memberId(memberId)
//                .publicKey(extractedPublicKey)
//                .build();
//        credentialRepository.save(credential);
//        return true;
        try {
            byte[] attestationBytes = Base64.getDecoder().decode(attestationObject);
            CBORFactory cborFactory = new CBORFactory();
            ObjectMapper cborMapper = new ObjectMapper(cborFactory);
            Map<String, Object> attestationMap = cborMapper.readValue(attestationBytes, Map.class);
            String fmt = (String) attestationMap.get("fmt");
            byte[] authData = (byte[]) attestationMap.get("authData");

            if (authData.length < 37) { // 최소 길이 체크: 32(RP ID 해시)+1(플래그)+4(서명 카운트)
                throw new IllegalArgumentException("authData 길이가 너무 짧습니다.");
            }

            int flags = authData[32] & 0xFF;
            // AT(Attested Credential Data) 플래그가 설정되어 있는지 확인 (0x40)
            boolean hasAttestedCredentialData = (flags & 0x40) != 0;
            if (!hasAttestedCredentialData) {
                throw new IllegalArgumentException("Attested Credential Data가 authData에 존재하지 않습니다.");
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

            // 6. credentialPublicKey를 CBOR로 디코딩 (필요시 상세 파싱 가능)
            Map<String, Object> publicKeyCose = cborMapper.readValue(publicKeyBytes, Map.class);
            // 여기서는 간단히 추출한 credentialPublicKey 전체를 base64로 인코딩하여 저장합니다.
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);

            // 7. 추출된 publicKey를 DB에 저장 (WebAuthnCredential 엔티티에 저장)
            WebAuthnCredential credential = WebAuthnCredential.builder()
                    .credentialId(credentialId)
                    .memberId(memberId)
                    .publicKey(publicKeyBase64)
                    .build();
            credentialRepository.save(credential);
            return true;
        }catch (Exception e) {
            // 에러 로깅 및 예외 처리
            e.printStackTrace();
            return false;
        }
    }

    public Optional<String> getCredentialIdByMemberId(Long memberId) {
        return credentialFetcher.fetchByMemberId(memberId)
                .map(credential -> credential.getCredentialId());
    }

    private String generateRandomChallenge() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
