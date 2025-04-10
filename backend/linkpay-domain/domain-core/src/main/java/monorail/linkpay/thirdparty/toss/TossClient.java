package monorail.linkpay.thirdparty.toss;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.thirdparty.toss.dto.TossConfirmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static monorail.linkpay.exception.ExceptionCode.SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class TossClient {

    public static final String TOSS_PAYMENTS_CONFIRM = "https://api.tosspayments.com/v1/payments/confirm";

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public void confirmPayment(final String paymentKey, final Long amount, final String orderId) {
        String credentials = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(UTF_8));
        TossConfirmRequest requestBody = new TossConfirmRequest(paymentKey, orderId, amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.set("Authorization", "Basic " + credentials);
        HttpEntity<TossConfirmRequest> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                TOSS_PAYMENTS_CONFIRM,
                HttpMethod.POST,
                httpEntity,
                Object.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LinkPayException(SERVER_ERROR, "결제 승인 실패= " + response.getStatusCode());
        }
    }
}
