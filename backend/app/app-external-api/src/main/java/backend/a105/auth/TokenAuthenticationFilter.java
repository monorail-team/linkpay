package backend.a105.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import static backend.a105.auth.TokenAuthentication.unauthenticated;
import static backend.a105.util.ObjectUtil.isNull;
import static backend.a105.util.StringUtil.substringAfter;

@Slf4j
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String header;
    private final String scheme;

    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                     AuthenticationManager authenticationManager,
                                     String header,
                                     String scheme) {
        super(requiresAuthenticationRequestMatcher,authenticationManager);
        this.header = header;
        this.scheme = scheme;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("attemptAuthentication");
        String token = extractToken(request);
        return super.getAuthenticationManager().authenticate(unauthenticated(token));
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(header);
        if (isNull(authHeader) || !authHeader.startsWith(scheme)) {
            throw new AuthenticationException("인증 헤더가 올바르지 않습니다 header = " + authHeader) {};
        }

        return substringAfter(authHeader, scheme).trim();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.debug("Handling authentication failure");
        log.debug("Failed to process authentication request", failed);
        SecurityContextHolder.clearContext();
        throw failed;
    }
}
