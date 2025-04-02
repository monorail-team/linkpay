package monorail.linkpay.banking.account.service;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.common.domain.Money;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(Long walletId, Long memberId) {
        return accountRepository.save(Account.builder()
                .id(idGenerator.generate())
                .walletId(walletId)
                .memberId(memberId)
                .money(new Money(0))
                .build()).getId();
    }

    @Transactional
    public void delete(Long walletId, Long memberId) {
        Account account = accountRepository.findByWalletId(walletId).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        if (!account.getMemberId().equals(memberId)) {
            throw new LinkPayException(INVALID_REQUEST, "계좌 삭제는 생성자만 할 수 있습니다.");
        }
        accountRepository.delete(account);
    }

    @Transactional
    public void deduct(Long walletId, Money money) {
        Account account = accountRepository.findByWalletId(walletId).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.deductPoint(money);
    }

    @Transactional
    public void withdrawal(Long walletId, Money money) {
        Account account = accountRepository.findByWalletId(walletId).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.withdrawalPoint(money);
    }
}
