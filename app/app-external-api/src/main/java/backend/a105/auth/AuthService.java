package backend.a105.auth;

import backend.a105.exception.AppException;
import backend.a105.layer.BusinessLayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static backend.a105.exception.ExceptionCode.INVALID_REQUEST;

@Slf4j
@BusinessLayer
@RequiredArgsConstructor
public class AuthService {
    public LoginResponse login(LoginRequest request) {
        log.debug("login process in progress : {}", request);
        if (request instanceof KakaoLoginRequest kakaoLoginRequest) {
            log.debug("카카오 로그인");
        }else{
            throw new AppException(INVALID_REQUEST, "지원하지 않는 타입의 로그인 요청");
        }

        log.debug("로그인 검증 진행");
        return LoginResponse.builder()
                .accessToken("엑세스 토큰")
                .build();
    }
}

