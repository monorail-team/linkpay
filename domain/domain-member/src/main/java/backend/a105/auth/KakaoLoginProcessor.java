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
        ResponseEntity<KakaoOauthResponse> authResponse = kakaoOauthClient.authorize(code);

        // 2xx 응답이면 JSON 파싱
        if (authResponse.getStatusCode().is4xxClientError()) {
            throw new AppException(ExceptionCode.INVALID_AUTHORIZATION_CODE, "유효하지 않은 카카오 OAuth 요청");
        }else if(authResponse.getStatusCode().is5xxServerError()) {
            throw new AppException(ExceptionCode.SERVER_ERROR, "카카오 서버 에러");
        }else if(!authResponse.getStatusCode().is2xxSuccessful()) {
            throw new AppException(ExceptionCode.SERVER_ERROR, "알 수 없는 에러");
        }

        String accessToken = authResponse.getBody().accessToken();

        ResponseEntity<KakaoUserResponse> userResponse = kakaoOauthClient.fetchUser(accessToken);
        if(userResponse.getStatusCode().is4xxClientError()){
            throw new AppException(ExceptionCode.INVALID_AUTHORIZATION_CODE, "로그인을 위한 권한이 부족");
        }else if(!userResponse.getStatusCode().is2xxSuccessful()) {
            throw new AppException(ExceptionCode.SERVER_ERROR, "알 수 없는 에러");
        }

        return Email.of(userResponse.getBody().kakaoAccount().email());
    }
}
