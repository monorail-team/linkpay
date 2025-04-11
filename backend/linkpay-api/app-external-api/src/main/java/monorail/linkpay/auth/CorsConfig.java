package monorail.linkpay.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    private final CorsProps corsProps;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProps.origins.toArray(new String[0]))
                .allowCredentials(true)
                .allowedMethods(corsProps.methods.toArray(new String[0]))
                .exposedHeaders(corsProps.headers.toArray(new String[0]));
    }
}
