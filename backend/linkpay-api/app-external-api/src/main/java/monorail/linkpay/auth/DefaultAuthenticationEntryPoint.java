package monorail.linkpay.auth;

import static monorail.linkpay.exception.ExceptionCode.UNAUTHORIZED_ACCESS_TOKEN;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public DefaultAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * @설명 인증 실패시 401 예외를 발생시킨다.
     * @주의 보통 여기서 sendResponse를 통해 직접적으로 응답을 생성하지만- ControllerAdvice를 통해 예외 처리 방법을 통일시키기 위해서- ExceptionResolver를 사용해 스프링
     * 컨테이너 내에서 예외를 처리시켰습니다.
     */
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        log.debug("Handle 401 Error: requestURI = {} {}", request.getMethod(), request.getRequestURI());
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                new LinkPayException(UNAUTHORIZED_ACCESS_TOKEN, authException.getMessage())
        );
    }
}
