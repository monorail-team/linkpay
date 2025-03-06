package backend.a105.auth;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
import backend.a105.layer.SupportLayer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@SupportLayer
@RequiredArgsConstructor
public class KakaoLoginProcessor {
    private final KakaoOauthClient kakaoOauthClient;

    public Email process(String code) {
        var authResponse = kakaoOauthClient.authorize(code);
        validate(authResponse, "유효하지 않은 카카오 OAuth 요청");

        var userResponse = kakaoOauthClient.fetchUser(authResponse.getBody().accessToken());
        validate(userResponse, "로그인을 위한 권한이 부족");

        return Email.of(userResponse.getBody().kakaoAccount().email());
    }

    private static void validate(ResponseEntity<?> response, String messageFor4xx) {
        // 2xx 응답이면 JSON 파싱
        if (response.getStatusCode().is4xxClientError()) {
            throw new AppException(ExceptionCode.INVALID_AUTHORIZATION_CODE, messageFor4xx);
        }else if(response.getStatusCode().is5xxServerError()) {
            throw new AppException(ExceptionCode.SERVER_ERROR, "카카오 서버 에러");
        }else if(!response.getStatusCode().is2xxSuccessful()) {
            throw new AppException(ExceptionCode.SERVER_ERROR, "알 수 없는 에러");
        }
    }
}
