package monorail.linkpay.fcm.service.dto;

import java.time.Instant;

public record FcmRegisterResponse(Instant expiresAt) {
}
