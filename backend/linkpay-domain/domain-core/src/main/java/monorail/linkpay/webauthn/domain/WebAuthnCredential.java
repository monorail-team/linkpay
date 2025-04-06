package monorail.linkpay.webauthn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "webauthn")
@Getter
@NoArgsConstructor
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
    public WebAuthnCredential(String credentialId, Long memberId, String publicKey) {
        this.credentialId = credentialId;
        this.memberId = memberId;
        this.publicKey = publicKey;
    }
}
