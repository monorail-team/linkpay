package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.AuthAuthenticateRequest;
import monorail.linkpay.controller.request.RegisterRequest;
import monorail.linkpay.webauthn.dto.RegisterChallengeResponse;
import monorail.linkpay.webauthn.dto.AuthChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnSuccessResponse;
import monorail.linkpay.webauthn.service.WebAuthnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webauthn")
public class WebAuthnController {

    private final WebAuthnService webAuthnService;

    @GetMapping("/register/challenge")
    public ResponseEntity<RegisterChallengeResponse> getRegisterChallenge() {
        String challenge = webAuthnService.getRegisterChallenge();
        return ResponseEntity.ok(new RegisterChallengeResponse(challenge));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@AuthenticationPrincipal final AuthPrincipal principal,
                                         @Valid @RequestBody final RegisterRequest request) {
        webAuthnService.registerAuthenticator(
                principal.memberId(),
                request.credentialId(),
                request.attestationObject()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 인증 챌린지 발급: 클라이언트가 인증 시작 전에 챌린지를 요청
    @GetMapping("/authenticate/challenge")
    public ResponseEntity<AuthChallengeResponse> getAuthChallenge(@AuthenticationPrincipal final AuthPrincipal principal) {
        var response = webAuthnService.getAuthChallenge(principal.memberId());
        return ResponseEntity.ok(response);
    }

    // 인증 수행: 클라이언트가 WebAuthn 데이터를 보내면 검증
    @PostMapping("/authenticate")
    public ResponseEntity<WebAuthnSuccessResponse> authenticate(@AuthenticationPrincipal final AuthPrincipal principal,
                                                                @Valid @RequestBody final AuthAuthenticateRequest request) {
        var response = webAuthnService.verifyAuthentication(
                principal.memberId(),
                request.credentialId(),
                request.clientDataJSON(),
                request.authenticatorData());
        return ResponseEntity.ok(response);
    }

}
