package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.PaymentConfirmRequest;
import monorail.linkpay.thirdparty.toss.TossClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thirdparty")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final TossClient tossClient;

    @PostMapping("/toss/checkout/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestBody final PaymentConfirmRequest request) {
        tossClient.confirmPayment(request.paymentKey(), request.amount(), request.orderId());
        return ResponseEntity.noContent().build();
    }
}
