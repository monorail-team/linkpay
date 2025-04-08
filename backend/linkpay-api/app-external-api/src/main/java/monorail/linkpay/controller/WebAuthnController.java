package monorail.linkpay.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.WebAuthnRequest;
import monorail.linkpay.controller.request.WebAuthnRegisterRequest;
import monorail.linkpay.webauthn.dto.WebAuthnRegisterChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnResponse;
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
    public ResponseEntity<WebAuthnRegisterChallengeResponse> getRegisterChallenge() {
        String challenge = webAuthnService.getRegisterChallenge();
        return ResponseEntity.ok(new WebAuthnRegisterChallengeResponse(challenge));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@AuthenticationPrincipal final AuthPrincipal principal,
                                         @Valid @RequestBody final WebAuthnRegisterRequest request) {
        webAuthnService.registerAuthenticator(
                principal.memberId(),
                request.credentialId(),
                request.attestationObject()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/authenticate/challenge")
    public ResponseEntity<WebAuthnChallengeResponse> getAuthChallenge(@AuthenticationPrincipal final AuthPrincipal principal) {
        var response = webAuthnService.getAuthChallenge(principal.memberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<WebAuthnResponse> authenticate(@AuthenticationPrincipal final AuthPrincipal principal,
                                                         @Valid @RequestBody final WebAuthnRequest request) {
        var response = webAuthnService.verifyAuthentication(
                principal.memberId(),
                request.credentialId(),
                request.clientDataJSON(),
                request.authenticatorData(),
                request.signature());

        return ResponseEntity.ok(response);
    }

}
