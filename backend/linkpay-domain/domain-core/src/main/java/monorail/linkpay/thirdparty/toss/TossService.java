package monorail.linkpay.thirdparty.toss;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TossService {

    private final TossClient tossClient;

    public void confirmPayment(final String paymentKey, final Long amount, final String orderId) {
        tossClient.confirmPayment(paymentKey, amount, orderId);
    }
}
