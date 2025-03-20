package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import monorail.linkpay.wallet.repository.WalletRepository;

import java.time.LocalDateTime;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryRecorder {

    private final WalletHistoryRepository walletHistoryRepository;
    private final WalletRepository walletRepository;
    private final IdGenerator idGenerator;

    public Long record(final Long walletId, final Long amount, final TransactionType transactionType) {
        Wallet wallet = walletRepository.findById(walletId)
            .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "지갑 아이디에 해당하는 지갑이 존재하지 않습니다."));

        return walletHistoryRepository.save(WalletHistory.builder()
            .id(idGenerator.generate())
            .point(new Point(amount))
            .wallet(wallet)
            .createdAt(LocalDateTime.now())
            .transactionType(transactionType)
            .build()).getId();
    }
}
