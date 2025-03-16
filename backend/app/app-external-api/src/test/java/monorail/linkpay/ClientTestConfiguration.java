package monorail.linkpay;

import monorail.linkpay.kakao.KakaoOauthClient;
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
