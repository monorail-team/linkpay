package monorail.linkpay.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static monorail.linkpay.auth.AuthTokenAuthentication.unauthenticated;
import static monorail.linkpay.util.ObjectUtil.isNull;
import static monorail.linkpay.util.StringUtil.substringAfter;

@Slf4j
public class AuthTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final String header;
    private final String scheme;

    public AuthTokenAuthenticationFilter(final AuthenticationManager authenticationManager,
                                         final String header,
                                         final String scheme) {
        this.authenticationManager = authenticationManager;
        this.header = header;
        this.scheme = scheme;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            log.debug("attemptAuthentication");
            Authentication authResult = attemptAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException e) {
            log.debug("Handling authentication failure");
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private Authentication attemptAuthentication(final HttpServletRequest request) {
        String token = extractToken(request);
        Authentication authResult = authenticationManager.authenticate(unauthenticated(token));
        return authResult;
    }

    private String extractToken(final HttpServletRequest request) {
        String authHeader = request.getHeader(header);
        if (isNull(authHeader) || !authHeader.startsWith(scheme)) {
            throw new AuthenticationException("인증 헤더를 포함시켜 요청해주세요: 인증 헤더 = " + authHeader) {
            };
        }
        return substringAfter(authHeader, scheme).trim();
    }

}
