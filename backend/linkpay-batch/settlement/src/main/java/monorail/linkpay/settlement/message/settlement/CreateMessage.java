package monorail.linkpay.settlement.message.settlement;

public record CreateMessage(Long messageId, Long amount, Long walletId, Long storeId) {
}
