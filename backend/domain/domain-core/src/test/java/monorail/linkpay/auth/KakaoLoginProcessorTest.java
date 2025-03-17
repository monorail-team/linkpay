package monorail.linkpay.auth;

import monorail.linkpay.auth.dto.LoginPrincipal;
import monorail.linkpay.auth.service.KakaoLoginProcessor;
import monorail.linkpay.kakao.KakaoOauthClient;
import monorail.linkpay.kakao.dto.KakaoOauthResponse;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KakaoLoginProcessorTest {

    @Test
    public void 카카오_인증_과정을_진행한다() throws Exception {
        //given
        String code = "카카오 인증 토큰";
        KakaoOauthClient kakaoOauthClient = mock(KakaoOauthClient.class);
        when(kakaoOauthClient.authorize(code))
                .thenReturn(ResponseEntity.ok(KakaoOauthResponse.of("accessToken")));
        when(kakaoOauthClient.fetchUser("accessToken"))
                .thenReturn(ResponseEntity.ok(KakaoUserResponse.of("kakao@kakao.com")));
        KakaoLoginProcessor sut = new KakaoLoginProcessor(kakaoOauthClient);

        //when
        LoginPrincipal loginPrincipal = sut.process(code);

        //then
        assertThat(loginPrincipal).isNotNull();
        assertThat(loginPrincipal.email()).isEqualTo("kakao@kakao.com");
    }

}