package monorail.linkpay.event.payload;

public record AccountWithdrawalEventPayload(Long walletId, long amount) implements EventPayload {
}