package monorail.linkpay.linkedwallet.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
