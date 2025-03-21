package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;

import java.time.LocalDateTime;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryRecorder {

    private final WalletHistoryRepository walletHistoryRepository;
    private final IdGenerator idGenerator;

    public Long record(final Wallet wallet,
                       final Point amount,
                       final Point remaining,
                       final TransactionType transactionType) {
        return walletHistoryRepository.save(WalletHistory.builder()
            .id(idGenerator.generate())
            .point(amount)
            .remaining(remaining)
            .wallet(wallet)
            .createdAt(LocalDateTime.now())
            .transactionType(transactionType)
            .build()).getId();
    }
}
