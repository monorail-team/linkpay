package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryRecorder {

    private final WalletHistoryRepository walletHistoryRepository;
    private final IdGenerator idGenerator;

    public void recordHistory(final TransactionType transactionType, final Wallet wallet,
                              final Point point, final Member member) {
        walletHistoryRepository.save(WalletHistory.builder()
                .id(idGenerator.generate())
                .amount(point)
                .remaining(wallet.getPoint())
                .transactionType(transactionType)
                .wallet(wallet)
                .member(member)
                .build());
    }
}
