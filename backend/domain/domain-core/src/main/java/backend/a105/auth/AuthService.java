package backend.a105.auth;

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
        var email = loginStrategyResolver.resolve(request);
        var loginMember = memberFetcher.fetchBy(email);
        var accessToken = authTokenGenerator.generateFor(loginMember, TokenType.ACCESS);

        log.debug("로그인 검증 진행");
        return LoginResponse.builder()
                .accessToken(accessToken.value())
                .build();
    }
}

