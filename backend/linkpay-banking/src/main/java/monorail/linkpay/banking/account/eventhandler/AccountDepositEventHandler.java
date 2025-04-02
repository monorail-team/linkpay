package monorail.linkpay.banking.account.eventhandler;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.common.domain.Money;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventType;
import monorail.linkpay.event.payload.AccountDepositEventPayload;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDepositEventHandler implements EventHandler<AccountDepositEventPayload> {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(final Event<AccountDepositEventPayload> event) {
        return EventType.DEPOSIT.equals(event.type());
    }

    @Override
    public void handle(final Event<AccountDepositEventPayload> event) {
        AccountDepositEventPayload payload = event.payload();
        Account account = accountRepository.findByWalletId(payload.walletId()).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.depositMoney(new Money(payload.amount()));
    }
}
