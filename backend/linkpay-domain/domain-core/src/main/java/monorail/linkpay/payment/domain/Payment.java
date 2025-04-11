package monorail.linkpay.payment.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.common.domain.BaseEntity;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.store.domain.Store;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "payment")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE payment SET deleted_at = CURRENT_TIMESTAMP WHERE payment_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Payment extends BaseEntity {

    @Id
    @Column(name = "payment_id")
    private Long id;

    @JoinColumn(name = "link_card_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkCard linkCard;

    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, updatable = false))
    private Point point;

    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @JoinColumn(name = "wallet_history_id", nullable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private WalletHistory walletHistory;

    @Builder
    private Payment(
            final Long id,
            final LinkCard linkCard,
            final Member member,
            final Point point,
            final Store store,
            final WalletHistory walletHistory
    ) {
        this.id = id;
        this.linkCard = linkCard;
        this.member = member;
        this.point = point;
        this.store = store;
        this.walletHistory = walletHistory;
    }

    public Long getStoreId() {
        return store.getId();
    }

    public Long getAmount() {
        return point.getAmount();
    }

    public Long getWalletId() {
        return linkCard.getWalletId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Payment payment)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
