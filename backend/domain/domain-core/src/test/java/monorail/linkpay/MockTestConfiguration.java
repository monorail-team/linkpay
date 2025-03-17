package monorail.linkpay;

import monorail.linkpay.jwt.JwtFixtures;
import monorail.linkpay.jwt.JwtProvider;
import monorail.linkpay.kakao.KakaoOauthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockTestConfiguration {
    @Primary
    @Bean(name = "kakaoOauthClient")
    public KakaoOauthClient mockKakaoOauthClient() {
        return mock(KakaoOauthClient.class);
    }

    @Primary
    @Bean(name = "jwtProvider")
    public JwtProvider stubJwtProvider() {return new JwtProvider(JwtFixtures.jwtProps);}
}
