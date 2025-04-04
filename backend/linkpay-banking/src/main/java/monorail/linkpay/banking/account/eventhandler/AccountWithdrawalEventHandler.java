package monorail.linkpay.banking.account.eventhandler;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.common.domain.Money;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.EventType;
import monorail.linkpay.event.payload.AccountWithdrawalEventPayload;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountWithdrawalEventHandler implements EventHandler<AccountWithdrawalEventPayload> {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(final Event<AccountWithdrawalEventPayload> event) {
        return EventType.WITHDRAWAL.equals(event.type());
    }

    @Override
    public void handle(final Event<AccountWithdrawalEventPayload> event) {
        AccountWithdrawalEventPayload payload = event.payload();
        Account account = accountRepository.findByWalletIdForUpdate(payload.walletId()).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.withdrawalMoney(new Money(payload.amount()));
    }
}
