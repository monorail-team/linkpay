package monorail.linkpay.linkedwallet.domain;

import jakarta.persistence.*;
import lombok.*;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;

import static lombok.AccessLevel.PROTECTED;

@Table(name = "linked_wallet")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class LinkedWallet extends BaseEntity {

    @Id
    @Column(name = "linked_wallet_id")
    private Long id;

    @Embedded
    private Point point;

    @Version
    private Integer version;

    @Builder
    public LinkedWallet(final Long id, final Point point) {
        this.id = id;
        this.point = point;
    }
}
