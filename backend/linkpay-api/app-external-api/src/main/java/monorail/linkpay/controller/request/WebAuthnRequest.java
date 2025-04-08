package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record WebAuthnRequest(
        @NotBlank(message = "자격 증명 ID를 입력해주세요.")
        String credentialId,

        @NotBlank(message = "clientDataJSON 값을 입력해주세요.")
        String clientDataJSON,

        @NotBlank(message = "authenticatorData 값을 입력해주세요.")
        String authenticatorData,

        @NotBlank(message = "signature 값을 입력해주세요.")
        String signature
) {}
