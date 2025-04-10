package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.PaymentConfirmRequest;
import monorail.linkpay.thirdparty.toss.TossService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/toss")
@RequiredArgsConstructor
public class TossController {

    private final TossService tossService;

    @PostMapping("/checkout/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody final PaymentConfirmRequest request) {
        tossService.confirmPayment(request.paymentKey(), request.amount(), request.orderId());
        return ResponseEntity.ok().build();
    }
}
