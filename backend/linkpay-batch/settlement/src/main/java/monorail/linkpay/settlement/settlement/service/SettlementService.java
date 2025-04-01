package monorail.linkpay.settlement.settlement.service;

import static monorail.linkpay.settlement.settlement.domain.SettlementStatus.PENDING;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.settlement.settlement.domain.Settlement;
import monorail.linkpay.settlement.settlement.repository.SettlementRepository;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final IdGenerator idGenerator;

    public Long create(final Long walletId, final Long storeId, final Long amount) {
        return settlementRepository.save(Settlement.builder()
                .id(idGenerator.generate())
                .walletId(walletId)
                .storeId(storeId)
                .amount(new Point(amount))
                .settlementStatus(PENDING)
                .build()).getId();
    }
}
