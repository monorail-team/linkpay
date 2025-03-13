package backend.a105.auth;

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
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var tokenAuthentication = (TokenAuthentication) authentication;
        var token = tokenAuthentication.getCredentials();
        // todo 검증 진행
        boolean success = token.equals("accessToken")? true: false;
        if(!success){
            log.debug("TokenAuthentication failed to authenticate");
            throw new AuthenticationException("msg") {};
        }
        AuthPrincipal principal = new AuthPrincipal(1L);
        List<GrantedAuthority> authorities = new ArrayList<>();
        return TokenAuthentication.authenticated(principal, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(TokenAuthentication.class);
    }
}
