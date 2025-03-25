package monorail.linkpay.linkcard.domain;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import java.util.Arrays;
import monorail.linkpay.exception.LinkPayException;

public enum CardState {

    REGISTERED,
    UNREGISTERED;

    public static CardState getCardState(final String state) {
        return Arrays.stream(CardState.values())
                .filter(c -> c.name().equalsIgnoreCase(state))
                .findFirst()
                .orElseThrow(() -> new LinkPayException(INVALID_REQUEST, "잘못된 카드 상태 값입니다."));
    }
}
