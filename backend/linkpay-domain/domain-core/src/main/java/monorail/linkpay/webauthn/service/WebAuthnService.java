package monorail.linkpay.webauthn.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
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
        // 실제로는 attestationObject를 검증하고 publicKey 추출 로직 필요
        String extractedPublicKey = "simulatedPublicKey";

        WebAuthnCredential credential = WebAuthnCredential.builder()
                .credentialId(credentialId)
                .memberId(memberId)
                .publicKey(extractedPublicKey)
                .build();
        credentialRepository.save(credential);
        return true;
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
