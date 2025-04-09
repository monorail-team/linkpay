package monorail.linkpay.webauthn.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.webauthn.domain.AuthnChallenge;
import monorail.linkpay.webauthn.repository.AuthnChallengeRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthnChallengeFetcher {

    private final AuthnChallengeRepository repository;

    public AuthnChallenge fetchByMemberId(final Long memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new LinkPayException(ExceptionCode.WEBAUTHN_NOT_REGISTERED, "등록되지않은 챌린지"));
    }
}
