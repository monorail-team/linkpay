package monorail.linkpay.banking.account.service;

import static monorail.linkpay.banking.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.banking.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.domain.Account;
import monorail.linkpay.banking.account.domain.Point;
import monorail.linkpay.banking.account.repository.AccountRepository;
import monorail.linkpay.banking.exception.LinkPayException;
import monorail.linkpay.banking.id.IdGenerator;
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
                .point(new Point(0))
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
    public void deduct(Long walletId, Point point) {
        Account account = accountRepository.findByWalletId(walletId).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.deductPoint(point);
    }

    @Transactional
    public void withdrawal(Long walletId, Point point) {
        Account account = accountRepository.findByWalletId(walletId).orElseThrow(() ->
                new LinkPayException(NOT_FOUND_RESOURCE, "계좌 아이디와 일치하는 계좌를 찾을 수 없습니다."));
        account.withdrawalPoint(point);
    }
}
