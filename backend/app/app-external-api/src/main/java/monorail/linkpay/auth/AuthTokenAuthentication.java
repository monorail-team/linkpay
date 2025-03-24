package monorail.linkpay.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class AuthTokenAuthentication extends CustomAuthentication {

    private final String credentials;

    public static AuthTokenAuthentication unauthenticated(final String token) {
        return new AuthTokenAuthentication(token);
    }

    public static AuthTokenAuthentication authenticated(final AuthPrincipal principal,
                                                        final Collection<? extends GrantedAuthority> authorities) {
        return new AuthTokenAuthentication(principal, authorities);
    }

    private AuthTokenAuthentication(final String token) {
        super(null, null);
        this.credentials = token;
        super.setAuthenticated(false);
    }

    private AuthTokenAuthentication(final AuthPrincipal principal,
                                    final Collection<? extends GrantedAuthority> authorities) {
        super(principal, authorities);
        this.credentials = null;
        super.setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return credentials;
    }
}
