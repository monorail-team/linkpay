package monorail.linkpay.exception;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ExceptionCode {

    INVALID_REQUEST(1001, BAD_REQUEST),
    INVALID_AUTHORIZATION_CODE(1002, BAD_REQUEST),
    INVALID_EVENT_TYPE(1003, BAD_REQUEST),

    NOT_FOUND_RESOURCE(2001, NOT_FOUND),
    DUPLICATED_USER_ID(2002, CONFLICT),
    DUPLICATED_RESOURCE(2003, CONFLICT),

    UNAUTHORIZED_ID(3001, UNAUTHORIZED),
    UNAUTHORIZED_PASSWORD(3002, UNAUTHORIZED),
    UNAUTHORIZED_USER(3003, UNAUTHORIZED),
    UNAUTHORIZED_ACCESS_TOKEN(3004, UNAUTHORIZED),
    UNAUTHORIZED_REFRESH_TOKEN(3005, UNAUTHORIZED),
    FORBIDDEN_ACCESS(3006, FORBIDDEN),

    BANK_API_FAILED(8000, INTERNAL_SERVER_ERROR),

    SERVER_ERROR(9000, INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;
}
