package monorail.linkpay.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(String accessToken) {
}
