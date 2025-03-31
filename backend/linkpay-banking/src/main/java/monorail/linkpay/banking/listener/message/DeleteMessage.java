package monorail.linkpay.banking.listener.message;

public record DeleteMessage(Long messageId, Long walletId, Long memberId) {
}
