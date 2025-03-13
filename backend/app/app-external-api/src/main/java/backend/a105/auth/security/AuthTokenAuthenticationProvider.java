package backend.a105.auth.security;

import backend.a105.auth.dto.AuthTokenPayload;
import backend.a105.token.TokenValidationException;
import backend.a105.token.TokenValidator;
import backend.a105.token.dto.ValidatedToken;
import backend.a105.util.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenAuthenticationProvider implements AuthenticationProvider {
    private final TokenValidator tokenValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var tokenAuthentication = (AuthTokenAuthentication) authentication;
            ValidatedToken validatedToken = tokenValidator.validate(tokenAuthentication.getCredentials());
            AuthTokenPayload payload = JsonUtil.parse(validatedToken.payload(), AuthTokenPayload.class);
            AuthPrincipal principal = new AuthPrincipal(payload.memberId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            return AuthTokenAuthentication.authenticated(principal, authorities);
        } catch (TokenValidationException e) {
            log.debug("TokenAuthentication failed to authenticate");
            throw new AuthenticationException(e.getMessage()) {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthTokenAuthentication.class);
    }
}
