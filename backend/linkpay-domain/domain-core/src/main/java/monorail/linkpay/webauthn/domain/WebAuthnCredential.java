package monorail.linkpay.webauthn.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "webauthn")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class WebAuthnCredential {
    /**
     * credentialId는 autoincrement가 아니라 실제로 사용하는 값이라 String으로 해놓았습니다.
     */
    @Id
    @Column(name = "credential_id")
    private String credentialId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Builder
    private WebAuthnCredential(final String credentialId, final Long memberId, final String publicKey) {
        this.credentialId = credentialId;
        this.memberId = memberId;
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WebAuthnCredential that)) {
            return false;
        }
        return getCredentialId() != null && Objects.equals(getCredentialId(), that.getCredentialId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCredentialId());
    }
}
