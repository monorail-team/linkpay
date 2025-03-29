package monorail.linkpay.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.controller.request.PaymentsRequest;
import monorail.linkpay.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> createPayment(@AuthenticationPrincipal final AuthPrincipal principal,
                                              @Valid @RequestBody final PaymentsRequest paymentsRequest) {
        paymentService.createPayment(
                principal.memberId(),
                new Point(paymentsRequest.amount()),
                paymentsRequest.linkCardId(),
                paymentsRequest.storeId()
        );
        return ResponseEntity.status(CREATED).build();
    }
}
