package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.fcm.service.FcmSender;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.service.LinkedMemberFetcher;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@SupportLayer
@RequiredArgsConstructor
@Slf4j
public class PaymentNotifier {

    private final FcmSender fcmSender;
    private final LinkedMemberFetcher linkedMemberFetcher;

    public void notifySuccess(final Long executorId,
                              final Store store,
                              final LinkCard linkCard,
                              final long amount) {

        LocalDateTime now = LocalDateTime.now();

        // 개인 결제 알림
        fcmSender.send(
                executorId,
                "✅ 결제가 성공했습니다",
                formatPersonalSuccessMessage(store, linkCard, amount, now)
        );

        // 공유 지갑이라면 추가 알림
        if (!(Hibernate.unproxy(linkCard.getWallet()) instanceof LinkedWallet linkedWallet)) {
            return;
        }

        List<LinkedMember> linkedMembers = linkedMemberFetcher.readAllByLinkedWalletId(linkedWallet.getId());

        Member payer = linkedMembers.stream()
                .map(LinkedMember::getMember)
                .filter(member -> Objects.equals(member.getId(), executorId))
                .findFirst()
                .orElse(null);

        if (isNull(payer)) {
            log.error("링크지갑 알림 실패: 링크지갑 ID={}, executorId={} → 해당 멤버 없음. 구성원 리스트: {}",
                    linkedWallet.getId(), executorId,
                    linkedMembers.stream()
                            .map(lm -> lm.getMember().getId() + ":" + lm.getMember().getUsername())
                            .toList());
            return;
        }

        String title = String.format("✅ %s 링크지갑에서 결제가 성공했습니다", linkedWallet.getName());

        linkedMembers.stream().filter(lm -> !Objects.equals(lm.getMember(), payer))
                .forEach(m -> {
                    fcmSender.send(
                            m.getMember().getId(),
                            title,
                            formatSharedSuccessMessage(payer.getUsername(), store, linkCard, amount, now)
                    );
                });
    }

    public void notifyFailure(final Long executorId,
                              final Exception ex) {
        fcmSender.send(
                executorId,
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

    private String formatSharedSuccessMessage(String payer, Store store, LinkCard linkCard, long amount, LocalDateTime time) {
        return String.format("""
                        결제자: %s
                        상점: %s
                        카드: %s
                        금액: %s
                        일시: %s
                        """,
                payer,
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