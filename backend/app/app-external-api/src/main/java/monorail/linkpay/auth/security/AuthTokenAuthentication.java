package monorail.linkpay.auth.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthTokenAuthentication extends CustomAuthentication {
    private final String credentials;


    public static AuthTokenAuthentication unauthenticated(String token) {
        return new AuthTokenAuthentication(token);
    }

    public static AuthTokenAuthentication authenticated(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        return new AuthTokenAuthentication(principal, authorities);
    }

    private AuthTokenAuthentication(String token) {
        super(null, null);
        this.credentials = token;
        super.setAuthenticated(false);
    }

    private AuthTokenAuthentication(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, authorities);
        this.credentials = null;
        super.setAuthenticated(true);
    }

    @Override
    public String getCredentials() {
        return credentials;
    }
}
