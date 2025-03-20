package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.domain.WalletHistory;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryService {

    private final WalletHistoryRepository walletHistoryRepository;
    private final WalletRepository walletRepository;
    private final IdGenerator idGenerator;

    public Long create(final Long walletId, final Long amount, final TransactionType transactionType) {
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

    public WalletHistoryResponse read(final Long walletHistoryId) {
        WalletHistory walletHistory = walletHistoryRepository.findById(walletHistoryId)
            .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "내역 아이디에 해당하는 내역이 존재하지 않습니다."));

        return new WalletHistoryResponse(walletHistory.getId(),
                walletHistory.getPoint().getAmount(),
                walletHistory.getTransactionType().toString(),
                walletHistory.getCreatedAt());
    }
}
