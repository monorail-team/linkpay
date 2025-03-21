package monorail.linkpay.security;

import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.auth.AuthTokenAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(final WithCustomUser withCustomUser) {
        long id = withCustomUser.id();

        AuthPrincipal principal = new AuthPrincipal(id);
        AuthTokenAuthentication auth = new AuthTokenAuthentication(principal, Collections.emptyList());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
