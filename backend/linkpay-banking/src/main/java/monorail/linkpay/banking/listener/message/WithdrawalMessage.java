package monorail.linkpay.banking.listener.message;

public record WithdrawalMessage(
        Long messageId,
        Long walletId,
        long amount
) {
}
