package monorail.linkpay.auth;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class CustomAuthentication extends AbstractAuthenticationToken {
    private final AuthPrincipal principal;

    public CustomAuthentication(AuthPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;

    }

    public AuthPrincipal getPrincipal(){
        return principal;
    }
}