package monorail.linkpay.webauthn.dto;

public record AuthChallengeResponse(String challenge,String credentialId) {}
