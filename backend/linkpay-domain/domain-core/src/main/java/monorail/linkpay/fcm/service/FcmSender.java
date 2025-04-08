package monorail.linkpay.fcm.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.client.FcmClient;
import monorail.linkpay.fcm.client.dto.FcmSendRequest;

@SupportLayer
@RequiredArgsConstructor
public class FcmSender {
    private final FcmClient fcmClient;
    private final FcmTokenFetcher fcmTokenFetcher;

    public void send(final Long memberId, final String title, final String content) {
        //todo FIX
        FcmToken fcmToken = fcmTokenFetcher.fetchAllByMemberId(memberId).stream().findFirst()
                .orElseThrow(() -> new LinkPayException(ExceptionCode.SERVER_ERROR, "dasdsa"));
        fcmClient.sendPush(FcmSendRequest.builder()
                .token(fcmToken.getToken())
                .title(title)
                .body(content)
                .build());
    }
}
