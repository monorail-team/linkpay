package backend.a105.auth.dto;

import lombok.Builder;

@Builder
public record AuthToken(String value) {
    public static AuthToken of(String value) {
        return AuthToken.builder()
                .value(value)
                .build();
    }
}
