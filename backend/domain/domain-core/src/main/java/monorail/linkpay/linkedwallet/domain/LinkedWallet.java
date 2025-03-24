package monorail.linkpay.linkedwallet.domain;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;

@Table(name = "linked_wallet")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class LinkedWallet extends BaseEntity {

    @Id
    @Column(name = "linked_wallet_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Point point;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "linkedWallet", cascade = ALL, orphanRemoval = true)
    private Set<LinkedMember> linkedMembers = new HashSet<>();

    @Builder
    public LinkedWallet(final Long id, final String name, final Set<LinkedMember> linkedMembers) {
        this.id = id;
        this.name = name;
        this.point = new Point(0);
        this.linkedMembers = linkedMembers;
    }

    public void registerLinkedMember(final LinkedMember linkedMember) {
        linkedMembers.add(linkedMember);
        linkedMember.registerToWallet(this);
    }
}
