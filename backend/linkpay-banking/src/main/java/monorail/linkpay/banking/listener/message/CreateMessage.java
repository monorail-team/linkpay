package monorail.linkpay.banking.listener.message;

public record CreateMessage(
        Long messageId,
        Long walletId,
        Long memberId
) {
}
