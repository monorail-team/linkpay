package monorail.linkpay.auth.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.auth.dto.KakaoLoginRequest;
import monorail.linkpay.auth.dto.LoginPrincipal;
import monorail.linkpay.auth.dto.LoginRequest;
import monorail.linkpay.exception.LinkPayException;

@Slf4j
@SupportLayer
@RequiredArgsConstructor
public class LoginStrategyResolver {

    private final KakaoLoginProcessor kakaoLoginProcessor;

    /**
     * @설명 로그인 요청 유형에 따라 적절한 LoginProcessor에게 로그인 과정을 위임
     * @생각[주빈] 로그인 방법이 다양화될 수 있다는 생각에 분리했습니다. 현재는 약간 오버엔지니어링이라는 생각이듭니다.
     */
    public LoginPrincipal resolve(final LoginRequest request) {
        if (request instanceof KakaoLoginRequest kakaoLoginRequest) {
            log.debug("카카오 로그인");
            return kakaoLoginProcessor.process(kakaoLoginRequest.code());
        }
        throw new LinkPayException(INVALID_REQUEST, "지원하지 않는 타입의 로그인 요청");
    }
}
