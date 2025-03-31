package monorail.linkpay.webauthn.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebAuthnCredentialFetcher {

    private final WebAuthnCredentialRepository repository;

    public Optional<WebAuthnCredential> fetchByMemberId(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    public Optional<WebAuthnCredential> fetchByCredentialId(String credentialId) {
        return repository.findById(credentialId);
    }
}
