package monorail.linkpay.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public AppException(final ExceptionCode exceptionCode, final String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
