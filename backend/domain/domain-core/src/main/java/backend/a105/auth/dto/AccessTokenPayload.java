package backend.a105.auth.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AccessTokenPayload(long memberId,
                                 List<String> roles) {
    // todo 할 일 1.패키지 정리, 2.jwt랑 토큰 테스트, 3. SpringSecurity Filter
}