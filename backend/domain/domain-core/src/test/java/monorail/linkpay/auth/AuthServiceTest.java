package monorail.linkpay.auth;

import monorail.linkpay.auth.dto.KakaoLoginRequest;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.auth.service.AuthService;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest extends IntegrationTest {

    @Autowired
    AuthService sut;
    @Autowired
    KakaoOauthClient mockKakaoOauthClient;
    @Autowired
    MemberRepository memberRepository;

    /**
     * @설명 로그인에 대한 테스트
     * @주의 현재는 카카오 로그인만 고려해서 작성했음
     * @생각[주빈] 추후에 Oauth나 자체 로그인 로직이 추가되면 @ParameterizedTest를 적용해야 할 것 같다
     */
    @Nested
    class 로그인_테스트 {
        @Test
        public void 로그인_성공_시_엑세스_토큰을_반환한다() throws Exception {
            //given
            String code = "code";
            String accessToken = "value";
            Member member = memberRepository.save(Member.builder()
                    .id(1L)
                    .username("link")
                    .email("email@kakao.com").build());
            when(mockKakaoOauthClient.authorize(code)).thenReturn(
                    ResponseEntity.ok(KakaoOauthResponse.of(accessToken))
            );
            when(mockKakaoOauthClient.fetchUser(accessToken)).thenReturn(
                    ResponseEntity.ok(KakaoUserResponse.of(member.getEmail()))
            );

            //when
            LoginResponse response = sut.login(new KakaoLoginRequest(code));

            //then
            assertThat(response.accessToken()).isNotBlank();
        }


        @Test
        public void 올바르지_않은_code_를_전달하면_로그인에_실패한다() throws Exception {
            //given
            var request = new KakaoLoginRequest("wrong_code");
            when(mockKakaoOauthClient.authorize(any())).thenReturn(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            );

            //when, then
            assertThatThrownBy(() -> sut.login(request))
                    .isInstanceOf(LinkPayException.class);
        }
    }
}