package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record FcmRegisterRequest(

        @NotNull
        String token,
        @NotNull
        String deviceId,
        @NotNull
        Instant expiresAt
) {
}
