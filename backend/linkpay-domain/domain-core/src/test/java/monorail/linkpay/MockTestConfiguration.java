package monorail.linkpay;

import static org.mockito.Mockito.mock;

import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.config.SshTunnelingInitializer;
import monorail.linkpay.jwt.JwtFixtures;
import monorail.linkpay.jwt.JwtProvider;
import monorail.linkpay.wallet.client.BankAccountClient;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@TestConfiguration
public class MockTestConfiguration {

    @Primary
    @Bean(name = "kakaoOauthClient")
    public KakaoOauthClient mockKakaoOauthClient() {
        return mock(KakaoOauthClient.class);
    }

    @Primary
    @Bean(name = "jwtProvider")
    public JwtProvider stubJwtProvider() {
        return new JwtProvider(JwtFixtures.jwtProps);
    }

    @Primary
    @Bean(name = "bankAccountClient")
    public BankAccountClient bankAccountClient() {
        return mock(BankAccountClient.class);
    }

    @Primary
    @Bean(name = "sshTunnelingInitializer")
    public SshTunnelingInitializer mockSshTunnelingInitializer() {
        return new SshTunnelingInitializer() {
            @Override
            public int buildRdsSSHConnection() {
                return 3306; // 테스트용 DB 포트
            }

            @Override
            public int buildRedisSSHConnection() {
                return 6379; // 테스트용 Redis 포트
            }
        };
    }

    @Primary
    @Bean
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .username("sa")
                .password("")
                .driverClassName("org.h2.Driver")
                .build();
    }
}
