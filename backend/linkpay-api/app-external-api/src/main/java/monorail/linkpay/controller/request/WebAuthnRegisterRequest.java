package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record WebAuthnRegisterRequest(
        @NotBlank(message = "자격 증명 ID를 입력해주세요.")
        String credentialId,

//        @NotBlank(message = "clientDataJSON 값을 입력해주세요.")
//        String clientDataJSON,

        @NotBlank(message = "attestationObject 값을 입력해주세요.")
        String attestationObject

) {}
