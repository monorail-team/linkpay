package monorail.linkpay.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.dto.LoginPrincipal;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import monorail.linkpay.auth.service.KakaoLoginProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class KakaoLoginProcessorTest {

    @Test
    public void 카카오_인증_과정을_진행한다() {
        //given
        String code = "카카오 인증 토큰";
        KakaoOauthClient kakaoOauthClient = mock(KakaoOauthClient.class);
        when(kakaoOauthClient.authorize(code))
                .thenReturn(ResponseEntity.ok(KakaoOauthResponse.of("accessToken")));
        when(kakaoOauthClient.fetchUser("accessToken"))
                .thenReturn(ResponseEntity.ok(KakaoUserResponse.of("kakao@kakao.com", "username")));
        KakaoLoginProcessor sut = new KakaoLoginProcessor(kakaoOauthClient);

        //when
        LoginPrincipal loginPrincipal = sut.process(code);

        //then
        assertThat(loginPrincipal).isNotNull();
        assertThat(loginPrincipal.email()).isEqualTo("kakao@kakao.com");
    }

}