package backend.a105.auth.service;

import backend.a105.annotation.SupportLayer;
import backend.a105.auth.dto.AuthTokenPayload;
import backend.a105.auth.dto.AuthToken;
import backend.a105.member.domain.Member;
import backend.a105.token.dto.GeneratedToken;
import backend.a105.token.TokenGenerator;
import backend.a105.token.TokenType;
import backend.a105.util.json.JsonUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@SupportLayer
@RequiredArgsConstructor
public class AuthTokenGenerator {
    private final TokenGenerator tokenGenerator;

    /**
    * @설명
    * member에 대한 식별자와 권한을 포함하는 인증/인가 토큰을 생성하는 메서드
    * @주의
    * @생각[주빈]
    * 당장은 권한에 대한 정책이 없어서 임시로 "member_role" 하드코딩했습니다.
    */
    public AuthToken generateFor(Member member, TokenType type) {
        AuthTokenPayload payload = AuthTokenPayload.builder()
                .memberId(member.getId())
                .roles(List.of("member_role"))
                .build();
        GeneratedToken generated = tokenGenerator.generate(type, JsonUtil.toJson(payload));
        return AuthToken.of(generated.value());
    }
}
