package monorail.linkpay.banking.account.eventhandler;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.common.event.Event;
import monorail.linkpay.banking.common.event.EventType;
import monorail.linkpay.banking.common.event.payload.AccountDeletedEventPayload;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDeletedEventHandler implements EventHandler<AccountDeletedEventPayload> {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(final Event<AccountDeletedEventPayload> event) {
        return EventType.DELETE.equals(event.type());
    }

    @Override
    public void handle(final Event<AccountDeletedEventPayload> event) {
        AccountDeletedEventPayload payload = event.payload();
        Account account = accountRepository.findByWalletId(payload.walletId()).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        if (!account.getMemberId().equals(payload.memberId())) {
            throw new LinkPayException(INVALID_REQUEST, "계좌 삭제는 생성자만 할 수 있습니다.");
        }
        accountRepository.delete(account);
    }
}
