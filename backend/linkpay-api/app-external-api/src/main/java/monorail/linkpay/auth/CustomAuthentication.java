package monorail.linkpay.auth;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public abstract class CustomAuthentication extends AbstractAuthenticationToken {

    private final AuthPrincipal principal;

    protected CustomAuthentication(final AuthPrincipal principal,
                                   final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
    }
}