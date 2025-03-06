package backend.a105.auth;

import lombok.Builder;

@Builder
public record KakaoLoginRequest(String code) implements LoginRequest {
}
