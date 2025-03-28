package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Repository;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Repository
@RequiredArgsConstructor
public class WalletFetcher {

    private final WalletRepository walletRepository;

    public Wallet fetchByIdForUpdate(final Long walletId) {
        return walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "지갑 아이디에 해당하는 지갑이 없습니다."));
    }
}
