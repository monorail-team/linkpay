package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.fcm.service.FcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerFcmToken(@AuthenticationPrincipal final AuthPrincipal principal,
                                                 @RequestParam("token") final String token) {
        fcmService.register(principal.memberId(), token);
        return ResponseEntity.noContent().build();
    }
}
