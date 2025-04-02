package monorail.linkpay.banking.account.domain;

import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.banking.common.domain.Money;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "account")
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE account SET deleted_at = CURRENT_TIMESTAMP WHERE account_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Account {

    @Id
    @Column(name = "account_id")
    private Long id;

    @Embedded
    private Money money;
    private Long walletId;
    private Long memberId;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;

    @Builder
    public Account(final Long id, final Long walletId, final Money money, final Long memberId) {
        this.id = id;
        this.walletId = walletId;
        this.money = money;
        this.memberId = memberId;
        this.createdAt = now();
    }

    public void depositMoney(final Money money) {
        this.money = this.money.add(money);
    }

    public void withdrawalMoney(final Money money) {
        this.money = this.money.subtract(money);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Account account)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
