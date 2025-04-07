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

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmClient {

    private final RestTemplate restTemplate;
    private final FcmProps props;
    private final FcmTokenProvider fcmTokenProvider;

    public ResponseEntity<String> sendPush(final FcmSendRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fcmTokenProvider.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        var notification = Map.of(
                "title", request.title(),
                "body", request.body()
        );
        var message = Map.of(
                "token", request.token(),
                "notification", notification
        );
        var payload = Map.of("message", message);

        log.debug("[FCM] 메시지 전송 요청: {}", payload);
        return restTemplate.postForEntity(props.apiUrl(), new HttpEntity<>(payload, headers), String.class);
    }
}