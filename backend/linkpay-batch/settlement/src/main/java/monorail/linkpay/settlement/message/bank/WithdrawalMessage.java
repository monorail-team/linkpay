package monorail.linkpay.settlement.message.bank;

public record WithdrawalMessage(Long messageId, Long walletId, long amount) {
}
