package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.history.domain.*;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;

import java.time.LocalDateTime;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryRecorder {

    private final WalletHistoryRepository walletHistoryRepository;
    private final IdGenerator idGenerator;

    public void recordWallet(final TransactionType transactionType,
                             final Wallet wallet,
                             final Point point) {
        walletHistoryRepository.save(WalletHistory.builder()
                .id(idGenerator.generate())
                .wallet(wallet)
                .amount(point)
                .remaining(wallet.getPoint())
                .transactionType(transactionType)
                .historyDate(LocalDateTime.now())
                .build());
    }

    public void recordLinkedWallet(TransactionType transactionType, LinkCard linkCard, LinkedWallet linkedWallet, LinkedMember linkedMember, Point point, String merchantName) {
        walletHistoryRepository.save(WalletHistory.builder()
                .merchantName(merchantName)
                .amount(point)
                .remaining(linkedWallet.getPoint())
                .transactionType(transactionType)
                .historyDate(LocalDateTime.now())
                .linkCard(linkCard)
                .linkedMember(linkedMember).build());
    }
}
