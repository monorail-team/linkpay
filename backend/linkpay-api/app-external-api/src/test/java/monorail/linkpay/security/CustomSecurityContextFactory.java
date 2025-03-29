package monorail.linkpay.security;

import static monorail.linkpay.auth.AuthTokenAuthentication.authenticated;

import java.util.Collections;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.auth.AuthTokenAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(final WithCustomUser withCustomUser) {
        AuthPrincipal principal = new AuthPrincipal(withCustomUser.id());
        AuthTokenAuthentication auth = authenticated(principal, Collections.emptySet());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
