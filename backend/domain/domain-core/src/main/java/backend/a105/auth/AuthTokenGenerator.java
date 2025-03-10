package backend.a105.auth;

import backend.a105.jwt.GeneratedToken;
import backend.a105.jwt.TokenGenerator;
import backend.a105.jwt.JwtType;
import backend.a105.layer.SupportLayer;
import backend.a105.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static backend.a105.auth.AccessTokenClaim.MEMBER_ID;
import static backend.a105.auth.AccessTokenClaim.ROLES;

@SupportLayer
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final TokenGenerator tokenGenerator;

    public AuthToken generateFor(Member member, JwtType type) {
        Map<String, Object> payload = Map.of(
                MEMBER_ID.value(), member.getId().value(),
                ROLES.value(), List.of()
        );
        GeneratedToken generatedToken = tokenGenerator.generate(type, payload);
        return AuthToken.of(generatedToken.value());
    }
}
