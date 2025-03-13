package backend.a105.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(String accessToken) {
}
