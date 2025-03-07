package backend.a105.auth;

import backend.a105.jwt.JwtProvider;
import backend.a105.jwt.JwtType;
import backend.a105.jwt.SignedJwt;
import backend.a105.layer.SupportLayer;
import backend.a105.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

@SupportLayer
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final JwtProvider jwtProvider;

    public AuthToken generateFor(Member member, JwtType type) {
        SignedJwt jwt = jwtProvider.generate(member.getId().value(), List.of(), type);
        return AuthToken.of(jwt.value());
    }
}
