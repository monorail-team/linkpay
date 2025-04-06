package monorail.linkpay.event.payload;

public record BankResponseEventPayload(Long outboxId) implements EventPayload {
}
