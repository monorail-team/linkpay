package monorail.linkpay.banking.account.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.banking.common.domain.BaseEntity;

@Table(name = "account")
@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Account extends BaseEntity {

    @Id
    @Column(name = "account_id")
    private Long id;

    @Embedded
    private Point point;

    private Long walletId;

    private Long memberId;

    @Builder
    public Account(final Long id, final Long walletId, final Point point, final Long memberId) {
        this.id = id;
        this.walletId = walletId;
        this.point = point;
        this.memberId = memberId;
    }

    public void deductPoint(Point point) {
        this.point = this.point.add(point);
    }

    public void withdrawalPoint(Point point) {
        this.point = this.point.subtract(point);
    }
}
