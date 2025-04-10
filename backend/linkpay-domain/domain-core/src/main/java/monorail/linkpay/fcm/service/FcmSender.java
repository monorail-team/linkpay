package monorail.linkpay.fcm.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.fcm.client.FcmClient;
import monorail.linkpay.fcm.client.dto.FcmSendRequest;
import monorail.linkpay.fcm.domain.FcmToken;
import org.springframework.scheduling.annotation.Async;

@Async
@SupportLayer
@RequiredArgsConstructor
@Slf4j
public class FcmSender {
    private final FcmClient fcmClient;
    private final FcmTokenFetcher fcmTokenFetcher;

    public void send(final Long memberId, final String title, final String content) {
        log.debug("Send FCM token to memberId: {}", memberId);
        List<FcmToken> fcmTokens = fcmTokenFetcher.fetchAllByMemberId(memberId).stream()
                .filter(token -> !token.isExpired())
                .toList();
        if (fcmTokens.isEmpty()) {
            log.error("등록된 사용자 FCM 토큰이 없습니다.");
            // TODO: 여기서 예외 던지면 결제 트랜잭션이 깨짐, requires new transaction 또는 비동기 메시징 처리할 경우에만 예외 더닞기
//            throw new LinkPayException(ExceptionCode.NOT_FOUND_RESOURCE, "등록된 사용자 FCM 토큰이 없습니다.");
            return;
        }
        fcmTokens.forEach(fcmToken -> fcmClient.sendPush(
                FcmSendRequest.builder()
                        .token(fcmToken.getToken())
                        .title(title)
                        .body(content)
                        .build())
        );
    }
}
