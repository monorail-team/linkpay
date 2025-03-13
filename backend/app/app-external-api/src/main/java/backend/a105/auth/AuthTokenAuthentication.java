package backend.a105.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthTokenAuthentication extends CustomAuthentication {
    private final String credentials;


    public static AuthTokenAuthentication unauthenticated(String token) {
        return new AuthTokenAuthentication(null, token, null);
    }

    public static AuthTokenAuthentication authenticated(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        return new AuthTokenAuthentication(principal, null, authorities);
    }

    private AuthTokenAuthentication(AuthPrincipal principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, authorities);
        this.credentials = token;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }
}
