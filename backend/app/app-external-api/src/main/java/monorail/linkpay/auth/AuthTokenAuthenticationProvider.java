package monorail.linkpay.auth;

import monorail.linkpay.auth.dto.AuthTokenPayload;
import monorail.linkpay.token.TokenValidationException;
import monorail.linkpay.token.TokenValidator;
import monorail.linkpay.token.dto.ValidatedToken;
import monorail.linkpay.util.json.JsonUtil;
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

    /**
    * @설명
    * 토큰 검증 과정을 수행한다.
    * @주의
    * 검증에 실패하면 AuthenticationException을 발생: 호출하는 부분에서 적절히 처리해야 한다.
    */
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
        return AuthTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
