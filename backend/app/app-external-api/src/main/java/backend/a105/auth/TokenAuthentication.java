package backend.a105.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication extends CustomAuthentication {
    private final String credentials;


    public static TokenAuthentication unauthenticated(String token) {
        return new TokenAuthentication(null, token, null);
    }

    public static TokenAuthentication authenticated(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        return new TokenAuthentication(principal, null, authorities);
    }

    private TokenAuthentication(AuthPrincipal principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, authorities);
        this.credentials = token;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }
}
