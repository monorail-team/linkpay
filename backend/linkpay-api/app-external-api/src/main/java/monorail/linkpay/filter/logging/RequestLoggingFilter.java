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

        String body = "[unreadable]";
        CachedBodyHttpServletRequest wrappedRequest = null;

        try {
            wrappedRequest = new CachedBodyHttpServletRequest(request);

            BufferedReader reader = wrappedRequest.getReader();
            if (reader != null) {
                body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } else {
                body = "[null reader]";
            }
        } catch (Exception e) {
            log.warn("Failed to read request body for uri: {}, error: {}", uri, e.getMessage());
        }

        log.info("Incoming Request: method = {}, uri = {}, body = {}", method, uri, body);

        if (wrappedRequest != null) {
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
