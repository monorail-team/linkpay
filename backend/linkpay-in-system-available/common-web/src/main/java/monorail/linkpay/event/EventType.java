package monorail.linkpay.event;

import static lombok.AccessLevel.PRIVATE;
import static monorail.linkpay.exception.ExceptionCode.INVALID_EVENT_TYPE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.event.payload.AccountDeletedEventPayload;
import monorail.linkpay.event.payload.AccountDepositEventPayload;
import monorail.linkpay.event.payload.AccountWithdrawalEventPayload;
import monorail.linkpay.event.payload.BankResponseEventPayload;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.exception.LinkPayException;

@Getter
@RequiredArgsConstructor
public enum EventType {

    DELETE(Topic.ACCOUNT_DELETE, AccountDeletedEventPayload.class),
    DEPOSIT(Topic.ACCOUNT_DEPOSIT, AccountDepositEventPayload.class),
    WITHDRAWAL(Topic.ACCOUNT_WITHDRAWAL, AccountWithdrawalEventPayload.class),
    BANK_RESPONSE(Topic.BANK_RESPONSE, BankResponseEventPayload.class);

    private final String topic;
    private final Class<? extends EventPayload> payloadClass;

    public static EventType from(final String type) {
        try {
            return valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new LinkPayException(INVALID_EVENT_TYPE, "지원하지 않는 이벤트 타입입니다.");
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    public static class Topic {
        public static final String ACCOUNT_DELETE = "account-delete";
        public static final String ACCOUNT_DEPOSIT = "account-deposit";
        public static final String ACCOUNT_WITHDRAWAL = "account-withdrawal";
        public static final String BANK_RESPONSE = "bank-response";
    }
}
