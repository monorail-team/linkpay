package monorail.linkpay.banking.common.event.payload;

public record AccountWithdrawalEventPayload(Long walletId, long amount) implements EventPayload {
}