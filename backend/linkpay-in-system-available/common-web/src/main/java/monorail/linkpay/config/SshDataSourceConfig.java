package monorail.linkpay.config;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.config.SshTunnelingInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class SshDataSourceConfig {

    private final SshTunnelingInitializer initializer;

    @Bean
//    @Primary
    public DataSource dataSource(final DataSourceProperties properties) {
        Integer forwardedPort = initializer.buildRdsSSHConnection();
        String url = properties.getUrl().replace("[forwardedPort]", String.valueOf(forwardedPort));
        return DataSourceBuilder.create()
                .url(url)
                .username(properties.getUsername())
                .password(properties.getPassword())
                .driverClassName(properties.getDriverClassName())
                .build();
    }
}