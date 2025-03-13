package backend.a105;

import backend.a105.jwt.JwtFixtures;
import backend.a105.jwt.JwtProvider;
import backend.a105.kakao.KakaoOauthClient;
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
