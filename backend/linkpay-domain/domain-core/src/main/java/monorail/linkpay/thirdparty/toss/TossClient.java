package monorail.linkpay.thirdparty.toss;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.thirdparty.toss.dto.TossConfirmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static monorail.linkpay.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class TossClient {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public void confirmPayment(String paymentKey, Long amount, String orderId) {
        String credentials = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        TossConfirmRequest requestBody = new TossConfirmRequest(paymentKey, orderId, amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.set("Authorization", "Basic " + credentials);
        HttpEntity<TossConfirmRequest> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/confirm",
                HttpMethod.POST,
                httpEntity,
                Object.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LinkPayException(SERVER_ERROR, "결제 승인 실패= " + response.getStatusCode());
        }
    }
}
