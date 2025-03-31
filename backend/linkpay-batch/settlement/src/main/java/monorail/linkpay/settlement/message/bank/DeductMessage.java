package monorail.linkpay.settlement.message.bank;

public record DeductMessage(Long messageId, Long walletId, long amount) {
}
