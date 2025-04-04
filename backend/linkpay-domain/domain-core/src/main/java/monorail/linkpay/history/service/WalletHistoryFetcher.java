package monorail.linkpay.history.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryFetcher {

    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistory fetchById(final Long id) {
        return walletHistoryRepository.findById(id)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "아이디에 해당하는 내역이 존재하지 않습니다."));
    }

    public Slice<WalletHistory> fetchPage(final Long walletId,
                                          final Long lastId,
                                          final int size) {
        return walletHistoryRepository
                .findByWalletIdWithLastId(walletId, lastId, PageRequest.of(0, size));
    }
}
