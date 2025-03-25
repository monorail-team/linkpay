package monorail.linkpay.settlement.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.settlement.domain.SettleStatus;
import monorail.linkpay.settlement.domain.SettleType;
import monorail.linkpay.settlement.domain.Settlement;
import monorail.linkpay.settlement.repository.SettlementRepository;
import monorail.linkpay.wallet.domain.Wallet;

import java.time.LocalDateTime;

@SupportLayer
@RequiredArgsConstructor
public class SettlementRecorder {

    private final SettlementRepository settlementRepository;

    public void recordWallet(SettleType settleType, Wallet wallet, Point point) {
        settlementRepository.save(Settlement.builder()
                .amount(point)
                .remaining(wallet.getPoint())
                .settleType(settleType)
                .settleStatus(SettleStatus.APPROVED)
                .settleDate(LocalDateTime.now())
                .build());
    }

    public void recordLinkedWallet(SettleType settleType, LinkCard linkCard, LinkedWallet linkedWallet, LinkedMember linkedMember, Point point, String merchantName) {
        settlementRepository.save(Settlement.builder()
                .merchantName(merchantName)
                .amount(point)
                .remaining(linkedWallet.getPoint())
                .settleType(settleType)
                .settleStatus(SettleStatus.APPROVED)
                .settleDate(LocalDateTime.now())
                .linkCard(linkCard)
                .linkedMember(linkedMember).build());
    }
}
