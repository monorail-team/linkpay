package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.AuthAuthenticateRequest;
import monorail.linkpay.controller.request.RegisterRequest;
import monorail.linkpay.webauthn.dto.RegisterChallengeResponse;
import monorail.linkpay.webauthn.dto.AuthChallengeResponse;
import monorail.linkpay.webauthn.dto.RegistrationStatusResponse;
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

    // 1. 인증 챌린지 발급: 클라이언트가 인증 시작 전에 챌린지를 요청
    @GetMapping("/auth-challenge")
    public ResponseEntity<AuthChallengeResponse> getAuthChallenge(@AuthenticationPrincipal final AuthPrincipal principal) {
        String challenge = webAuthnService.generateAuthChallenge(principal.memberId());
        String credentialId = webAuthnService.getCredentialIdByMemberId(principal.memberId()).orElse("");
        return ResponseEntity.ok(new AuthChallengeResponse(challenge, credentialId));
    }

    // 2. 인증 수행: 클라이언트가 WebAuthn 데이터를 보내면 검증
    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticate(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final AuthAuthenticateRequest request) {
        boolean verified = webAuthnService.verifyAuthentication(
                principal.memberId(),
                request.credentialId(),
                request.clientDataJSON(),
                request.authenticatorData()
        );
        if (!verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    // 3. 등록 상태 조회: 현재 사용자가 WebAuthn 등록이 되어있는지 확인
    @GetMapping("/registration-status")
    public ResponseEntity<RegistrationStatusResponse> getRegistrationStatus(@AuthenticationPrincipal final AuthPrincipal principal) {
        boolean registered = webAuthnService.isRegistered(principal.memberId());
        return ResponseEntity.ok(new RegistrationStatusResponse(registered));
    }

    // 4. 등록 챌린지 발급: 클라이언트가 등록 시작 전에 챌린지를 요청
    @GetMapping("/register-challenge")
    public ResponseEntity<RegisterChallengeResponse> getRegisterChallenge(@AuthenticationPrincipal final AuthPrincipal principal) {
        String challenge = webAuthnService.generateRegisterChallenge(principal.memberId());
        return ResponseEntity.ok(new RegisterChallengeResponse(challenge));
    }

    // 5. 등록 수행: 클라이언트가 WebAuthn 등록 데이터를 보내면 처리
    @PostMapping("/register")
    public ResponseEntity<Void> register(@AuthenticationPrincipal final AuthPrincipal principal,
                                         @Valid @RequestBody final RegisterRequest request) {
        boolean success = webAuthnService.registerAuthenticator(
                principal.memberId(),
                request.credentialId(),
                request.clientDataJSON(),
                request.attestationObject()
        );
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
