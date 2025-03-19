package monorail.linkpay.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.exception.ExceptionCode.SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException e,
        final HttpHeaders headers,
        final HttpStatusCode status,
        final WebRequest request
    ) {
        log.warn(e.getMessage(), e);
        final String errorMessage = Objects.requireNonNull(
            e.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
        final LinkPayException linkPayException = new LinkPayException(INVALID_REQUEST, errorMessage);
        final ExceptionCode exceptionCode = linkPayException.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), linkPayException.getMessage()));
    }

    @ExceptionHandler(LinkPayException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(final LinkPayException e) {
        log.warn(e.getMessage(), e);
        final ExceptionCode exceptionCode = e.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);
        final LinkPayException linkPayException = new LinkPayException(SERVER_ERROR, "서버 오류가 발생했습니다.");
        final ExceptionCode exceptionCode = linkPayException.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), linkPayException.getMessage()));
    }
}
