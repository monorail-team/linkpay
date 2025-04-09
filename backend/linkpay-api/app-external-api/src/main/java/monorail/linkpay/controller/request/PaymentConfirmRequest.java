package monorail.linkpay.controller.request;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount
) {
}
