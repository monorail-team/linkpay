package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryFetcher {

    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistory fetchById(final Long id) {
        return walletHistoryRepository.findById(id)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "내역 아이디에 해당하는 내역이 존재하지 않습니다."));
    }
}
