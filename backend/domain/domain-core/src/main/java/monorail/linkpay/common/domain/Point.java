package monorail.linkpay.common.domain;

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
public class Point {

    private long amount;

    public Point(final long amount) {
        if (amount < 0) {
            throw new LinkPayException(INVALID_REQUEST, "금액은 음수가 될 수 없습니다.");
        }
        this.amount = amount;
    }

    public Point add(final Point point) {
        return new Point(this.amount + point.amount);
    }

    public Point subtract(final Point point) {
        if (this.amount < point.amount) {
            throw new LinkPayException(INVALID_REQUEST, "차감할 금액은 잔액보다 작거나 같은 값이어야 합니다.");
        }
        return new Point(this.amount - point.amount);
    }

    public Point multiply(final long value) {
        if (value < 1) {
            throw new LinkPayException(INVALID_REQUEST, "곱할 값은 1 이상이어야 합니다.");
        }
        return new Point(this.amount * value);
    }

    public Point divide(final long value) {
        if (value < 1) {
            throw new LinkPayException(INVALID_REQUEST, "나눌 값은 1 이상이어야 합니다.");
        }
        return new Point(this.amount / value);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final Point point)) {
            return false;
        }
        return amount == point.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
