package monorail.linkpay.banking.listener.message;

public record DeductMessage(
        Long messageId,
        Long walletId,
        long amount
) {
}
