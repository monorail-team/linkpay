package monorail.linkpay.webauthn.dto;

import lombok.Builder;

@Builder
public record WebAuthnChallengeResponse(String challenge, String credentialId) {}
