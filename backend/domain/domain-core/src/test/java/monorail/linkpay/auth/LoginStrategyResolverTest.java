package monorail.linkpay.auth;

import monorail.linkpay.auth.dto.LoginPrincipal;
import monorail.linkpay.auth.dto.LoginRequest;
import monorail.linkpay.auth.service.KakaoLoginProcessor;
import monorail.linkpay.auth.service.LoginStrategyResolver;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.auth.dto.KakaoLoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginStrategyResolverTest {

    @Test
    public void 로그인_전략에_맞는_객체를_호출한다() {
        //given
        String code = "카카오_OAuth_토큰";
        KakaoLoginRequest request = KakaoLoginRequest.of(code);

        KakaoLoginProcessor kakaoLoginProcessor = Mockito.mock(KakaoLoginProcessor.class);
        LoginStrategyResolver sut = new LoginStrategyResolver(kakaoLoginProcessor);
        when(kakaoLoginProcessor.process(code))
                .thenReturn(LoginPrincipal.of("test@email.com"));

        //when
        LoginPrincipal loginPrincipal = sut.resolve(request);

        //then
        verify(kakaoLoginProcessor).process(code);
        assertThat(loginPrincipal.email()).isEqualTo("test@email.com");
    }

    @Test
    public void 지원하지_않는_전략인_경우_예외를_반환한다() {
        //given
        LoginStrategyResolver sut = new LoginStrategyResolver(Mockito.mock(KakaoLoginProcessor.class));
        LoginRequest 지원하지_않는_로그인_요청 = new LoginRequest() {};

        //when, then
        Assertions.assertThatThrownBy(() -> sut.resolve(지원하지_않는_로그인_요청))
                .isInstanceOf(LinkPayException.class)
                .extracting(e -> ((LinkPayException) e).getExceptionCode())
                .isEqualTo(ExceptionCode.INVALID_REQUEST);
    }
}