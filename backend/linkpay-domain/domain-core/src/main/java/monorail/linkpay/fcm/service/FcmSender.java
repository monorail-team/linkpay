package monorail.linkpay.fcm.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
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
            throw new LinkPayException(ExceptionCode.NOT_FOUND_RESOURCE, "등록된 사용자 FCM 토큰이 없습니다.");
        }

        fcmTokens.stream()
                .map(FcmToken::getToken).distinct()
                .forEach(token -> fcmClient.sendPush(
                        FcmSendRequest.builder()
                                .token(token)
                                .title(title)
                                .body(content)
                                .build())
                );
    }
}
