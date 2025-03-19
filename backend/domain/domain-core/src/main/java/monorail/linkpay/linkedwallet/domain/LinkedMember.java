package monorail.linkpay.linkedwallet.domain;

import jakarta.persistence.*;
import lombok.*;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.member.domain.Member;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "linked_member")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class LinkedMember extends BaseEntity {

    @Id
    @Column(name = "linked_member_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    @Enumerated(STRING)
    private Role role;

    @JoinColumn(name = "linked_wallet_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkedWallet linkedWallet;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public LinkedMember(final Long id, final Role role, final LinkedWallet linkedWallet, final Member member) {
        this.id = id;
        this.role = role;
        this.linkedWallet = linkedWallet;
        this.member = member;
    }
}
