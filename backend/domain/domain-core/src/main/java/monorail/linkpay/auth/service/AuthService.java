package monorail.linkpay.auth.service;

import monorail.linkpay.auth.dto.LoginRequest;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.token.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final LoginStrategyResolver loginStrategyResolver;
    private final MemberFetcher memberFetcher;
    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final AuthTokenGenerator authTokenGenerator;
    private final IdGenerator idGenerator;

    public LoginResponse login(final LoginRequest request) {
        log.debug("login process in progress : {}", request);
        var loginPrincipal = loginStrategyResolver.resolve(request);
        var loginMember = memberRepository.findByEmail(loginPrincipal.email())
            .orElseGet(() -> {
                Member member = memberRepository.save(Member.builder()
                        .id(idGenerator.generate())
                        .email(loginPrincipal.email())
                        .username("")
                        .build()
                );
                walletRepository.save(Wallet.builder()
                        .id(idGenerator.generate())
                        .point(new Point(0))
                        .member(member)
                        .build());
                return member;
            });



        var accessToken = authTokenGenerator.generateFor(loginMember, TokenType.ACCESS);

        log.debug("로그인 검증 진행");
        return LoginResponse.builder()
                .accessToken(accessToken.value())
                .build();
    }
}

