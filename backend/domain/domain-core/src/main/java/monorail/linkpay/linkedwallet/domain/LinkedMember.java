package monorail.linkpay.linkedwallet.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.member.domain.Member;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "linked_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"linked_wallet_id", "member_id"}))
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is null")
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

    public static LinkedMember of(final Member member, final Long id, final Role role) {
        return LinkedMember.builder()
                .id(id)
                .role(role)
                .member(member)
                .build();
    }

    public void registerToWallet(final LinkedWallet linkedWallet) {
        this.linkedWallet = linkedWallet;
    }

    public boolean isCreator() {
        return this.role == Role.CREATOR;
    }
}
