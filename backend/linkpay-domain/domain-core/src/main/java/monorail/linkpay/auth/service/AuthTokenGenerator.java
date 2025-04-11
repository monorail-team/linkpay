package monorail.linkpay.auth.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.auth.dto.AuthToken;
import monorail.linkpay.auth.dto.AuthTokenPayload;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.token.TokenGenerator;
import monorail.linkpay.token.TokenType;
import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.util.json.JsonUtil;

@SupportLayer
@RequiredArgsConstructor
public class AuthTokenGenerator {

    private final TokenGenerator tokenGenerator;

    /**
     * @설명 member에 대한 식별자와 권한을 포함하는 인증/인가 토큰을 생성하는 메서드
     * @주의
     * @생각[주빈] 당장은 권한에 대한 정책이 없어서 임시로 "member_role" 하드코딩했습니다.
     */
    public AuthToken generateFor(final Member member, final TokenType type) {
        AuthTokenPayload payload = AuthTokenPayload.builder()
                .memberId(member.getId())
                .roles(List.of("member_role"))
                .build();
        GeneratedToken generated = tokenGenerator.generate(type, JsonUtil.toJson(payload));
        return AuthToken.of(generated.value());
    }
}
