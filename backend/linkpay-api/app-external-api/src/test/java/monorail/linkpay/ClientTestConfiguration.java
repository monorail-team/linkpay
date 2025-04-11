package monorail.linkpay;

import static org.mockito.Mockito.mock;

import monorail.linkpay.auth.kakao.KakaoOauthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ClientTestConfiguration {

    @Primary
    @Bean
    public KakaoOauthClient mockKakaoOauthClient() {
        return mock(KakaoOauthClient.class);
    }
}
