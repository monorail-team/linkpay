package monorail.linkpay.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum ExceptionCode {

    INVALID_REQUEST(1001, BAD_REQUEST),
    INVALID_AUTHORIZATION_CODE(1002, BAD_REQUEST),

    NOT_FOUND_RESOURCE(2001, NOT_FOUND),
    DUPLICATED_USER_ID(2002, CONFLICT),
    DUPLICATED_RESOURCE(2003, CONFLICT),

    UNAUTHORIZED_ID(3001, UNAUTHORIZED),
    UNAUTHORIZED_PASSWORD(3002, UNAUTHORIZED),
    UNAUTHORIZED_USER(3003, UNAUTHORIZED),
    UNAUTHORIZED_ACCESS_TOKEN(3004, UNAUTHORIZED),
    UNAUTHORIZED_REFRESH_TOKEN(3005, UNAUTHORIZED),
    FORBIDDEN_ACCESS(3006, FORBIDDEN),

    SERVER_ERROR(9000, INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;
}
