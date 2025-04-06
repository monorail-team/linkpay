package monorail.linkpay.banking.common.domain;

import static lombok.AccessLevel.PROTECTED;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import monorail.linkpay.exception.LinkPayException;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Money {

    private long amount;

    public Money(final long amount) {
        this.amount = amount;
    }

    public Money add(final Money money) {
        return new Money(this.amount + money.amount);
    }

    public Money subtract(final Money money) {
        return new Money(this.amount - money.amount);
    }

    public Money multiply(final long value) {
        return new Money(this.amount * value);
    }

    public Money divide(final long value) {
        if (value < 1) {
            throw new LinkPayException(INVALID_REQUEST, "나눌 값은 1 이상이어야 합니다.");
        }
        return new Money(this.amount / value);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Money money)) {
            return false;
        }
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
