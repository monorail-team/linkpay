package backend.a105.auth.service;

import backend.a105.annotation.SupportLayer;
import backend.a105.auth.dto.AccessTokenPayload;
import backend.a105.auth.dto.AuthToken;
import backend.a105.member.domain.Member;
import backend.a105.token.dto.GeneratedToken;
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
                .memberId(member.getId())
                .roles(List.of())
                .build();
        GeneratedToken generated = tokenGenerator.generate(type, JsonUtil.toJson(payload));
        return AuthToken.of(generated.token());
    }
}
