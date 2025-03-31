package monorail.linkpay.banking.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ExceptionCode {

    INVALID_REQUEST(1001),
    INVALID_AUTHORIZATION_CODE(1002),

    NOT_FOUND_RESOURCE(2001),
    DUPLICATED_USER_ID(2002),
    DUPLICATED_RESOURCE(2003),

    UNAUTHORIZED_ID(3001),
    UNAUTHORIZED_PASSWORD(3002),
    UNAUTHORIZED_USER(3003),
    UNAUTHORIZED_ACCESS_TOKEN(3004),
    UNAUTHORIZED_REFRESH_TOKEN(3005),
    FORBIDDEN_ACCESS(3006),

    SERVER_ERROR(9000);

    private final int code;
}
