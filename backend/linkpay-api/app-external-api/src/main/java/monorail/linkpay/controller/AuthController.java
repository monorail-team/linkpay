package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.auth.dto.KakaoLoginRequest;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> loginWithKakao(@RequestParam("code") final String code) {
        log.debug("카카오 로그인 요청 code = {}", code);
        LoginResponse response = authService.login(new KakaoLoginRequest(code));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.SET_COOKIE, refreshCookie)
                .body(response);
    }

    @GetMapping("/test-authenticate")
    public ResponseEntity<Void> testAuthenticate() {
        return ResponseEntity.ok().build();
    }
}
