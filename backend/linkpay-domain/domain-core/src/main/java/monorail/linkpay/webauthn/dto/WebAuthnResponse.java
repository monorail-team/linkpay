package monorail.linkpay.webauthn.dto;

import lombok.Builder;

@Builder
public record WebAuthnResponse(String paymentToken) {
}
