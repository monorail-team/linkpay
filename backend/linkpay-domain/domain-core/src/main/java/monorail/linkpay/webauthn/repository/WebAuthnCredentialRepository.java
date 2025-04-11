package monorail.linkpay.webauthn.repository;

import java.util.Optional;
import monorail.linkpay.webauthn.domain.WebAuthnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAuthnCredentialRepository extends JpaRepository<WebAuthnCredential, String> {

    Optional<WebAuthnCredential> findByMemberId(Long memberId);
}
