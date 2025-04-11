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

@Table(name = "challenge")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class AuthnChallenge {

    @Id
    @Column(name = "member_Id")
    private Long memberId;

    @Column(name = "challenge", nullable = false)
    private String challenge;

    @Builder
    private AuthnChallenge(final Long memberId, final String challenge) {
        this.memberId = memberId;
        this.challenge = challenge;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AuthnChallenge that)) {
            return false;
        }
        return getMemberId() != null && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMemberId());
    }
}
