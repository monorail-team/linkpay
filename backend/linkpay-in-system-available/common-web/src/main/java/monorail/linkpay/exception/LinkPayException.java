package monorail.linkpay.exception;

import lombok.Getter;

@Getter
public class LinkPayException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public LinkPayException(final ExceptionCode exceptionCode, final String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
