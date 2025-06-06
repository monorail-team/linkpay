package monorail.linkpay.banking.common.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("banking")
@Configuration
@RequiredArgsConstructor
public class SshDataSourceConfig {

    private final SshTunnelingInitializer initializer;

    @Primary
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