package backend.a105.auth;

import backend.a105.layer.PresentationLayer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequestMapping("/api/auth")
@PresentationLayer
public class AuthController {

    @PostMapping("/login/kakao")
    public ResponseEntity<AuthResponse> loginWithKakao(@Valid @NotBlank @RequestParam("code") String code) {
        log.info("POST 카카오 로그인 요청 code = {}", code);
//
//        AuthTokens authTokens = authService.login(KakaoLogin.builder()
//                .code(code)
//                .build());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.SET_COOKIE, refreshCookie)
                .body(AuthResponse.builder()
                        .accessToken("token")
                        .build());
    }

    @GetMapping("/test-authenticate")
    public ResponseEntity<Void> testAuthenticate() {
        return ResponseEntity.ok().build();
    }
}
