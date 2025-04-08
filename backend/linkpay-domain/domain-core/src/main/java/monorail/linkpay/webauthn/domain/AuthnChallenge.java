package monorail.linkpay.webauthn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge")
@Getter
@NoArgsConstructor
public class AuthnChallenge {
    @Id
    @Column(name = "member_Id")
    private Long memberId;

    @Column(name = "challenge", nullable = false)
    private String challenge;

    @Builder
    public AuthnChallenge( Long memberId, String challenge) {
        this.memberId = memberId;
        this.challenge = challenge;
    }
}
