package monorail.linkpay.fcm.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.fcm.client.dto.FcmSendRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmClient {

    private final RestTemplate restTemplate;
    private final FcmProps props;
    private final FcmTokenProvider fcmTokenProvider;

    /**
     * Ref.
     * https://firebase.google.com/docs/reference/fcm/rest/v1/ErrorCode?hl=ko&_gl=1*1uqa46t*_up*MQ..*_ga*MjE1NDU5Mi4xNzQ0MTU4NDQ5*_ga_CW55HF8NVT*MTc0NDE1ODQ0OS4xLjAuMTc0NDE1ODQ0OS4wLjAuMA..
     */
    public ResponseEntity<String> sendPush(final FcmSendRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fcmTokenProvider.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        var notification = Map.of(
                "title", request.title(),
                "body", request.body()
        );
        Map<String, Object> webpushNotification = new HashMap<>();
        webpushNotification.put("vibrate", List.of(200, 100, 200));
        webpushNotification.put("requireInteraction", true);
        webpushNotification.put("tag", "transaction-tag");

        Map<String, Object> webpush = Map.of("notification", webpushNotification);
        Map<String, Object> message = Map.of(
                "token", request.token(),
                "notification", notification,
                "webpush", webpush
        );
        Map<String, Object> payload = Map.of("message", message);
        // TODO 응답 및 예외 처리
        log.debug("[FCM] 메시지 전송 요청: {}", payload);
        return restTemplate.postForEntity(props.apiUrl(), new HttpEntity<>(payload, headers), String.class);
    }
}