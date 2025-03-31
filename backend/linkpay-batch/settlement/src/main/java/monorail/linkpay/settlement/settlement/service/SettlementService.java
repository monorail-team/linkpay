package monorail.linkpay.settlement.settlement.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.settlement.common.domain.Point;
import monorail.linkpay.settlement.id.IdGenerator;
import monorail.linkpay.settlement.settlement.domain.Settlement;
import monorail.linkpay.settlement.settlement.repository.SettlementRepository;
import org.springframework.stereotype.Service;

import static monorail.linkpay.settlement.settlement.domain.SettlementStatus.PENDING;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final IdGenerator idGenerator;

    public Long create(Long walletId, Long storeId, Long amount) {
        return settlementRepository.save(Settlement.builder()
                .id(idGenerator.generate())
                .walletId(walletId)
                .storeId(storeId)
                .amount(new Point(amount))
                .settlementStatus(PENDING).build()).getId();
    }
}
