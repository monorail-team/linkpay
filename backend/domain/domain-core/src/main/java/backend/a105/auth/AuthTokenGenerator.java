package backend.a105.auth;

import backend.a105.layer.SupportLayer;
import backend.a105.member.domain.Member;
import backend.a105.token.GeneratedToken;
import backend.a105.token.TokenGenerator;
import backend.a105.token.TokenType;
import backend.a105.util.JsonUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@SupportLayer
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final TokenGenerator tokenGenerator;

    public AuthToken generateFor(Member member, TokenType type) {
        AccessTokenPayload payload = AccessTokenPayload.builder()
                .memberId(member.getId().value())
                .roles(List.of())
                .build();
        GeneratedToken generated = tokenGenerator.generate(type, JsonUtil.toJson(payload));
        return AuthToken.of(generated.token());
    }
}
