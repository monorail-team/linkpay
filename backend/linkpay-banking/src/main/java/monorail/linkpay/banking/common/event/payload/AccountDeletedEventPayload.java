package monorail.linkpay.banking.common.event.payload;

public record AccountDeletedEventPayload(Long walletId, Long memberId) implements EventPayload {
}
