package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.fcm.service.FcmSender;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.store.domain.Store;

import java.time.LocalDateTime;

@SupportLayer
@RequiredArgsConstructor
@Slf4j
public class PaymentNotifier {

    private final FcmSender fcmSender;

    public void notifySuccess(final Long executorId,
                              final Store store,
                              final LinkCard linkCard,
                              final long amount) {
        if (linkCard.getCardType() == CardType.SHARED) {
            // TODO: LinkedWallet의 Card인 경우 모두에게 알림 전송
        }

        String title = "✅ 결제가 성공했습니다";
        String content = String.format(
                """
                   상점: %s
                   
                   카드: %s
                   
                   금액: %s
                   
                   일시: %s     
                """,
                store.getName(),
                linkCard.getCardName(),
                amount,
                LocalDateTime.now()
        );

        fcmSender.send(executorId, title, content);
    }

    public void notifyFailure(final Long executorId,
                              final Exception ex) {
        String title = "❌ 결제가 실패했습니다";
        String content = String.format(
                """
                    사유: %s
                    
                    일시: %s      
                """,
                ex.getMessage(),
                LocalDateTime.now()
        );
        fcmSender.send(executorId, title, content);
    }
}
