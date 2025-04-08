package monorail.linkpay.webauthn.repository;

import monorail.linkpay.webauthn.domain.AuthnChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthnChallengeRepository extends JpaRepository<AuthnChallenge, Long> {
    Optional<AuthnChallenge> findByMemberId(Long memberId);
}
