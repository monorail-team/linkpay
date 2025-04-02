package monorail.linkpay.event.payload;

public record AccountDepositEventPayload(Long walletId, long amount) implements EventPayload {
}
