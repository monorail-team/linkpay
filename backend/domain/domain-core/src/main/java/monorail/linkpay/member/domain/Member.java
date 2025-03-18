package monorail.linkpay.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "member")
@Getter
@EqualsAndHashCode(of = "memberId", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Id
    private Long memberId;
    private String email;
    private String username;
    private String fingerprint;

    @Builder
    public Member(final Long memberId, final String email, final String username, final String fingerprint) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.fingerprint = fingerprint;
    }
}
