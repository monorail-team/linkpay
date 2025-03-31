package monorail.linkpay;

import static org.mockito.Mockito.mock;

import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.jwt.JwtFixtures;
import monorail.linkpay.jwt.JwtProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
}
