package monorail.linkpay.banking.common.event.payload;

public record AccountDepositEventPayload(Long walletId, long amount) implements EventPayload {
}
