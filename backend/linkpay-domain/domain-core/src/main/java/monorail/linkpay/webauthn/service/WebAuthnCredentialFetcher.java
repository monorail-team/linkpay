package monorail.linkpay.webauthn.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import monorail.linkpay.webauthn.repository.WebAuthnCredentialRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebAuthnCredentialFetcher {

    private final WebAuthnCredentialRepository repository;

    public WebAuthnCredential fetchByMemberId(Long memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(()->new LinkPayException(ExceptionCode.WEBAUTHN_NOT_REGISTERED, "등록되지 않은 인증 정보"));
    }

    public Optional<WebAuthnCredential> fetchByCredentialId(String credentialId) {
        return repository.findById(credentialId);
    }
}
