package backend.a105.auth;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoLoginProcessorTest {

    @Test
    public void 카카오_인증_과정을_진행한다() throws Exception{
        //given
        String code = "카카오 인증 토큰";
        KakaoOauthClint kakaoOauthClint = Mockito.mock(KakaoOauthClint.class);
        KakaoLoginProcessor sut = new KakaoLoginProcessor(kakaoOauthClint);

        //when
        Email email = sut.process(code);

        //then
        assertThat(email).isNotNull();
        assertThat(email.value()).isEqualTo("kakao@kakao.com");
    }

}