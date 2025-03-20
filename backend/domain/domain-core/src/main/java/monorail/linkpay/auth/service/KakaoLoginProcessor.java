package monorail.linkpay.auth.service;

import monorail.linkpay.auth.dto.LoginPrincipal;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@SupportLayer
@RequiredArgsConstructor
public class KakaoLoginProcessor {

    private final KakaoOauthClient kakaoOauthClient;

    /**
    * @설명
    * 실제로 KakaoOAuth를 통한 로그인을 진행한다.
    * @주의
    * 메서드를 읽거나 사용할 때, 주의할 점
    *
    * @생각[]
    * 외부 API를 호출하는 부분과 직접적으로 맞닿아 있는 객체
     * 외부 API를 호출하는 부분에 대한 표준적인 방법을 고민중입니다.
     * 현재는 다음과 같은 3가지 역할을 수행합니다.
     * 1. Client객체를 사용해 외부 API 호출
     * 2. 호출 결과를 검증
     * 3. 호출 결과를 적절한 객체로 매핑
    */
    public LoginPrincipal process(final String code) {
        var authResponse = kakaoOauthClient.authorize(code);
        validate(authResponse, "유효하지 않은 카카오 OAuth 요청");

        var userResponse = kakaoOauthClient.fetchUser(authResponse.getBody().accessToken());
        validate(userResponse, "로그인을 위한 권한이 부족");

        return LoginPrincipal.of(userResponse.getBody().kakaoAccount().email());
    }

    private static void validate(final ResponseEntity<?> response, final String messageFor4xx) {
        // 2xx 응답이면 JSON 파싱
        if (response.getStatusCode().is4xxClientError()) {
            throw new LinkPayException(ExceptionCode.INVALID_AUTHORIZATION_CODE, messageFor4xx);
        }else if(response.getStatusCode().is5xxServerError()) {
            throw new LinkPayException(ExceptionCode.SERVER_ERROR, "카카오 서버 에러");
        }else if(!response.getStatusCode().is2xxSuccessful()) {
            throw new LinkPayException(ExceptionCode.SERVER_ERROR, "알 수 없는 에러");
        }
    }
}
