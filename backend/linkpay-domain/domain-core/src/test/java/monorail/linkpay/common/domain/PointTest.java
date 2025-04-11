package monorail.linkpay.common.domain;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import monorail.linkpay.exception.LinkPayException;
import org.junit.jupiter.api.Test;

class PointTest {

    private static final Point ONE_THOUSAND = new Point(1000);
    private static final Point THREE_THOUSAND = new Point(3000);
    private static final Point FOUR_THOUSAND = new Point(4000);

    @Test
    void 금액이_음수면_예외가_발생한다() {
        assertThatThrownBy(() -> new Point(-1))
                .isInstanceOf(LinkPayException.class)
                .hasMessage("금액은 음수가 될 수 없습니다.")
                .extracting("exceptionCode.code")
                .isEqualTo(INVALID_REQUEST.getCode());
    }

    @Test
    void 곱셈_나눗셈_파라미터_검증() {
        Point point = new Point(1000);
        assertSoftly(
                softly -> {
                    softly.assertThatThrownBy(() -> point.multiply(0))
                            .isInstanceOf(LinkPayException.class)
                            .hasMessage("곱할 값은 1 이상이어야 합니다.")
                            .extracting("exceptionCode.code")
                            .isEqualTo(INVALID_REQUEST.getCode());
                    softly.assertThatThrownBy(() -> point.divide(0))
                            .isInstanceOf(LinkPayException.class)
                            .hasMessage("나눌 값은 1 이상이어야 합니다.")
                            .extracting("exceptionCode.code")
                            .isEqualTo(INVALID_REQUEST.getCode());
                });
    }

    @Test
    void 금액을_계산한다() {
        Point point = new Point(2000);
        assertSoftly(
                softly -> {
                    softly.assertThat(point.add(ONE_THOUSAND)).isEqualTo(THREE_THOUSAND);
                    softly.assertThat(point.subtract(ONE_THOUSAND)).isEqualTo(ONE_THOUSAND);
                    softly.assertThat(point.divide(2)).isEqualTo(ONE_THOUSAND);
                    softly.assertThat(point.multiply(2)).isEqualTo(FOUR_THOUSAND);
                }
        );
    }
}