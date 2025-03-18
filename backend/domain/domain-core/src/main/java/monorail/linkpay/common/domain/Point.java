package monorail.linkpay.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Point(

    @Column(nullable = false)
    long amount
) {

    public Point {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
    }

    public Point add(Point other) {
        return new Point(this.amount + other.amount);
    }

    public Point subtract(Point other) {
        return new Point(this.amount - other.amount);
    }

    public Point multiply(long factor) {
        return new Point(this.amount * factor);
    }

    public Point divide(long factor) {
        return new Point(this.amount / factor);
    }
}
