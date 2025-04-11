package monorail.linkpay.webauthn.service;

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

    public WebAuthnCredential fetchByMemberId(final Long memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new LinkPayException(ExceptionCode.WEBAUTHN_NOT_REGISTERED, "등록되지 않은 인증 정보"));
    }

    public WebAuthnCredential fetchByCredentialId(final String credentialId) {
        return repository.findById(credentialId)
                .orElseThrow(() -> new LinkPayException(ExceptionCode.WEBAUTHN_NOT_REGISTERED, "등록되지 않은 인증 정보"));
    }
}
