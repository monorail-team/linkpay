package backend.a105.auth;

import backend.a105.exception.AppException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest()
class AuthServiceTest {

    @Autowired
    AuthService authService;

    /**
     * @설명
     * 로그인에 대한 테스트
     * @주의
     * 현재는 카카오 로그인만 고려해서 작성했음
     *
     * @생각[주빈]
     * 추후에 Oauth나 자체 로그인 로직이 추가되면 @ParameterizedTest를 적용해야 할 것 같다
     */
    @Nested
    class 로그인_테스트{
        @Test
        public void 로그인_성공_시_엑세스_토큰을_반환한다() throws Exception{
            //given
            var request = new KakaoLoginRequest("code");

            //when
            LoginResponse sut = authService.login(request);

            //then
            assertThat(sut.accessToken()).isNotBlank();
        }


        @Test
        public void 올바르지_않은_code_를_전달하면_로그인에_실패한다() throws Exception{
            //given
            var request = new KakaoLoginRequest("wrong_code");

            //when, then
            assertThatThrownBy(()->authService.login(request))
                    .isInstanceOf(AppException.class);
        }
    }
}