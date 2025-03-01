package backend.a105.common.exception;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
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

import static backend.a105.exception.ExceptionCode.INVALID_REQUEST;
import static backend.a105.exception.ExceptionCode.SERVER_ERROR;

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
        final AppException appException = new AppException(INVALID_REQUEST, errorMessage);
        final ExceptionCode exceptionCode = appException.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), appException.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(final AppException e) {
        log.warn(e.getMessage(), e);
        final ExceptionCode exceptionCode = e.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);
        final AppException appException = new AppException(SERVER_ERROR, "서버 오류가 발생했습니다.");
        final ExceptionCode exceptionCode = appException.getExceptionCode();
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(new ExceptionResponse(exceptionCode.getCode(), appException.getMessage()));
    }
}
