package monorail.linkpay.event.payload;

public record AccountDeletedEventPayload(Long walletId, Long memberId) implements EventPayload {
}
