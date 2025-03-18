package monorail.linkpay.controller;

import monorail.linkpay.auth.dto.KakaoLoginRequest;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> loginWithKakao(@RequestParam("code") String code) {
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
