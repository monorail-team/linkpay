package monorail.linkpay.banking.account.domain;

import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.banking.common.domain.Money;

@Table(name = "account")
@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class Account {

    @Id
    @Column(name = "account_id")
    private Long id;

    @Embedded
    private Money money;

    private Long walletId;

    private Long memberId;

    private LocalDateTime createdAt;

    @Builder
    public Account(final Long id, final Long walletId, final Money money, final Long memberId) {
        this.id = id;
        this.walletId = walletId;
        this.money = money;
        this.memberId = memberId;
        this.createdAt = now();
    }

    public void deductPoint(Money money) {
        this.money = this.money.add(money);
    }

    public void withdrawalPoint(Money money) {
        this.money = this.money.subtract(money);
    }
}
