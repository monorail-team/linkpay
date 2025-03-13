package backend.a105.auth;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class CustomAuthentication extends AbstractAuthenticationToken {
    private final AuthPrincipal principal;

    public CustomAuthentication(AuthPrincipal principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(false);
    }

    public CustomAuthentication(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);

    }

    public AuthPrincipal getPrincipal(){
        return principal;
    }
}