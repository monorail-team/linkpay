package monorail.linkpay.webauthn.repository;

import java.util.Optional;
import monorail.linkpay.webauthn.domain.AuthnChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthnChallengeRepository extends JpaRepository<AuthnChallenge, Long> {

    Optional<AuthnChallenge> findByMemberId(Long memberId);
}
