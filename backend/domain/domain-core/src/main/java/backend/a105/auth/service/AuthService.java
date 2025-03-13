package backend.a105.auth.service;

import backend.a105.auth.dto.LoginRequest;
import backend.a105.auth.dto.LoginResponse;
import backend.a105.member.service.MemberFetcher;
import backend.a105.token.TokenType;
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

