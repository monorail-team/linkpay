package backend.a105.auth;

import backend.a105.exception.AppException;
import backend.a105.annotation.SupportLayer;
import backend.a105.type.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static backend.a105.exception.ExceptionCode.INVALID_REQUEST;

@Slf4j
@SupportLayer
@RequiredArgsConstructor
public class LoginStrategyResolver {

    private final KakaoLoginProcessor kakaoLoginProcessor;

    public Email resolve(LoginRequest request) {
        if (request instanceof KakaoLoginRequest kakaoLoginRequest) {
            log.debug("카카오 로그인");
            return kakaoLoginProcessor.process(kakaoLoginRequest.code());
        }

        throw new AppException(INVALID_REQUEST, "지원하지 않는 타입의 로그인 요청");
    }
}
