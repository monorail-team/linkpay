package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.fcm.service.FcmSender;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.service.LinkedMemberFetcher;
import monorail.linkpay.wallet.service.LinkedWalletFetcher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Async
@SupportLayer
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentNotifier {

    private final FcmSender fcmSender;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final LinkedMemberFetcher linkedMemberFetcher;

    public void notifySuccess(final Long payerId,
                              final Store store,
                              final LinkCard linkCard,
                              final Long walletId,
                              final Point point) {

        LocalDateTime now = LocalDateTime.now();

        // 개인 결제 알림
        fcmSender.send(
                payerId,
                "✅ 결제가 성공했습니다",
                formatPersonalSuccessMessage(store, linkCard, point.getAmount(), now)
        );

        log.info("결제 linkcard type = {}", linkCard.getCardType());
        // 공유 지갑이라면 추가 알림
        if (linkCard.getCardType() != CardType.SHARED) {
            log.info("링크카드가 아니므로 리턴");
            return;
        }

        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(walletId);
        List<LinkedMember> linkedMembers = linkedMemberFetcher.readAllByLinkedWalletId(linkedWallet.getId());

        Member payer = linkedMembers.stream()
                .map(LinkedMember::getMember)
                .filter(member -> Objects.equals(member.getId(), payerId))
                .findFirst()
                .orElse(null);


        if (isNull(payer)) {
            log.error("링크지갑 알림 실패: 링크지갑 ID={}, executorId={} → 해당 멤버 없음. 구성원 리스트: {}",
                    linkedWallet.getId(), payerId,
                    linkedMembers.stream()
                            .map(lm -> lm.getMember().getId() + ":" + lm.getMember().getUsername())
                            .toList());
            return;
        }

        log.debug("payer = {}", payer.getUsername());

        String title = String.format("✅ %s 링크지갑에서 결제가 성공했습니다", linkedWallet.getName());

        linkedMembers.stream().filter(lm -> !Objects.equals(lm.getMember().getId(), payer.getId()))
                .forEach(m -> {
                    fcmSender.send(
                            m.getMember().getId(),
                            title,
                            formatSharedSuccessMessage(payer.getUsername(), store, linkCard, point.getAmount(), now)
                    );
                });
    }

    public void notifyFailure(final Long payerId,
                              final Exception ex) {
        fcmSender.send(
                payerId,
                "❌ 결제가 실패했습니다",
                formatFailureMessage(ex)
        );
    }

    private String formatPersonalSuccessMessage(Store store, LinkCard linkCard, long amount, LocalDateTime time) {
        return String.format("""
                        상점: %s
                        카드: %s
                        금액: %s
                        일시: %s
                        """,
                store.getName(),
                linkCard.getCardName(),
                amount,
                time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }

    private String formatSharedSuccessMessage(String payerName, Store store, LinkCard linkCard, long amount, LocalDateTime time) {
        return String.format("""
                        결제자: %s
                        상점: %s
                        카드: %s
                        금액: %s
                        일시: %s
                        """,
                payerName,
                store.getName(),
                linkCard.getCardName(),
                amount,
                time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }

    private String formatFailureMessage(Exception ex) {
        return String.format("""
                        사유: %s
                        일시: %s
                        """,
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
        );
    }
}