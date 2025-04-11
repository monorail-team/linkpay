package monorail.linkpay.filter.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/actuator/**") ||
                requestURI.startsWith("/favicon.ico") ||
                requestURI.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        // 간단한 header 로깅 (선택)
        String headers = List.of(request.getHeaderNames()).stream()
                .map(name -> name + "=" + request.getHeader(String.valueOf(name)))
                .collect(Collectors.joining(", "));

        log.info("Incoming Request: method={}, uri={}{}{} | headers=[{}]",
                method,
                uri,
                (queryString != null ? "?" + queryString : ""),
                (request.getContentType() != null ? " [Content-Type=" + request.getContentType() + "]" : ""),
                headers);

        filterChain.doFilter(request, response);
    }
}
