package monorail.linkpay.banking.common.exception;

import static java.time.ZonedDateTime.now;

import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final int code;
    private final String message;
    private final ZonedDateTime timestamp;

    public ExceptionResponse(final int code, final String message) {
        this.code = code;
        this.message = message;
        this.timestamp = now();
    }
}
