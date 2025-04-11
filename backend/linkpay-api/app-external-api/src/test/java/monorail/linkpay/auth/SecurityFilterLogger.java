package monorail.linkpay.auth;

import jakarta.servlet.Filter;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityFilterLogger {

    public SecurityFilterLogger(final ApplicationContext applicationContext) {
        FilterChainProxy filterChainProxy = applicationContext.getBean(FilterChainProxy.class);
        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();

        for (SecurityFilterChain chain : filterChains) {
            for (Filter filter : chain.getFilters()) {
                System.out.println(filter.getClass().getName());
            }
        }
    }
}
