package backend.a105.auth;

import backend.a105.layer.SupportLayer;
import backend.a105.member.domain.Member;

@SupportLayer
public class AuthTokenGenerator {
    public AuthToken generateFor(Member member) {
        return AuthToken.of("accessToken");
    }
}
