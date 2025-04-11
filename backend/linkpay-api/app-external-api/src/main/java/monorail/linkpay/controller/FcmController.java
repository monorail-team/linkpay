package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.FcmRegisterRequest;
import monorail.linkpay.fcm.service.FcmService;
import monorail.linkpay.fcm.service.dto.FcmRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;;

    @PutMapping("/register")
    public ResponseEntity<FcmRegisterResponse> registerFcmToken(@AuthenticationPrincipal final AuthPrincipal principal,
                                                                @Valid @RequestBody FcmRegisterRequest request) {
        var response = fcmService.register(principal.memberId(), request.token(), request.deviceId());
        return ResponseEntity.ok(response);
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
