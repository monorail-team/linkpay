package backend.a105.auth;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
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
        Email email = new Email("test@email.com");
        MemberId memberId = new MemberId(1L);
        when(kakaoLoginProcessor.process(code))
                .thenReturn(LoginCandidate.builder()
                        .email(email)
                        .memberId(memberId)
                        .build());

        //when
        LoginCandidate loginCandidate = sut.resolve(request);

        //then
        verify(kakaoLoginProcessor).process(code);
        assertThat(loginCandidate.email().value()).isEqualTo("test@email.com");
        assertThat(loginCandidate.memberId().value()).isEqualTo(1L);
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