package monorail.linkpay.auth.service;

import monorail.linkpay.auth.dto.LoginRequest;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.token.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final LoginStrategyResolver loginStrategyResolver;
    private final MemberFetcher memberFetcher;
    private final AuthTokenGenerator authTokenGenerator;

    public LoginResponse login(LoginRequest request) {
        log.debug("login process in progress : {}", request);
        var loginPrincipal = loginStrategyResolver.resolve(request);
        var loginMember = memberFetcher.fetchBy(loginPrincipal.email());
        var accessToken = authTokenGenerator.generateFor(loginMember, TokenType.ACCESS);

        log.debug("로그인 검증 진행");
        return LoginResponse.builder()
                .accessToken(accessToken.value())
                .build();
    }
}

