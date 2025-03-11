package backend.a105.auth;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
import backend.a105.type.Email;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginStrategyResolverTest {

    @Test
    public void 로그인_전략에_맞는_객체를_호출한다() throws Exception {
        //given
        String code = "카카오_OAuth_토큰";
        KakaoLoginRequest request = KakaoLoginRequest.of(code);

        KakaoLoginProcessor kakaoLoginProcessor = Mockito.mock(KakaoLoginProcessor.class);
        LoginStrategyResolver sut = new LoginStrategyResolver(kakaoLoginProcessor);
        when(kakaoLoginProcessor.process(code))
                .thenReturn(Email.of("test@email.com"));

        //when
        Email email = sut.resolve(request);

        //then
        verify(kakaoLoginProcessor).process(code);
        assertThat(email.value()).isEqualTo("test@email.com");
    }

    @Test
    public void 지원하지_않는_전략인_경우_예외를_반환한다() throws Exception{
        //given
        LoginStrategyResolver sut = new LoginStrategyResolver(Mockito.mock(KakaoLoginProcessor.class));
        LoginRequest 지원하지_않는_로그인_요청 = new LoginRequest() {};

        //when, then
        Assertions.assertThatThrownBy(() -> sut.resolve(지원하지_않는_로그인_요청))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getExceptionCode())
                .isEqualTo(ExceptionCode.INVALID_REQUEST);
    }
}