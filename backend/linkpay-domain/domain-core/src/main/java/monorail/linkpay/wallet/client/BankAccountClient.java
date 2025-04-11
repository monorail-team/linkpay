package monorail.linkpay.wallet.client;

import static monorail.linkpay.exception.ExceptionCode.BANK_API_FAILED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.dto.AccountCreateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BankAccountClient {

    private final RestTemplate restTemplate;

    @Value("${banking.account.uri}")
    private String bankAccountUri;

    public void createAccount(final Long walletId, final Long memberId) {
        AccountCreateRequest requestBody = new AccountCreateRequest(walletId, memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<AccountCreateRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                bankAccountUri,
                HttpMethod.POST,
                requestEntity,
                Object.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LinkPayException(BANK_API_FAILED, "은행 계좌 생성 실패: status = " + response.getStatusCode());
        }
    }
}
