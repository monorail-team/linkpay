package monorail.linkpay.common.exception;

import lombok.Getter;
import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.*;

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
