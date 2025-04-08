package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.fcm.service.FcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;;

    @PostMapping("/register") // TODO: PUT Method?
    public ResponseEntity<Void> registerFcmToken(@AuthenticationPrincipal final AuthPrincipal principal,
                                                 @RequestParam("token") final String token) {
        fcmService.register(principal.memberId(), token);
        return ResponseEntity.noContent().build();
    }

    @Deprecated // 테스트용
    @PostMapping("/send")
    public ResponseEntity<Void> sendFcm(@RequestBody FcmSendRequest request){
        fcmService.sendmessgae(request.memberId(), request.title(), request.content());
        return ResponseEntity.noContent().build();
    }

    public record  FcmSendRequest(Long memberId, String title, String content) {
    }
}
