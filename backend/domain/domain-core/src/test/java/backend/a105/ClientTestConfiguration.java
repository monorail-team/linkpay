package backend.a105;

import backend.a105.auth.KakaoOauthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ClientTestConfiguration {
    @Bean
    @Primary
    public KakaoOauthClient mockKakaoOauthClient() {
        return mock(KakaoOauthClient.class);
    }
}
