package backend.a105.auth;

import lombok.Builder;

@Builder
public record LoginResponse(String accessToken) {
}
