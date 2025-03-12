package backend.a105.auth.dto;

import lombok.Builder;

@Builder
public record KakaoLoginRequest(String code) implements LoginRequest {
    public static KakaoLoginRequest of(String code) {
        return KakaoLoginRequest.builder().code(code).build();
    }
}
