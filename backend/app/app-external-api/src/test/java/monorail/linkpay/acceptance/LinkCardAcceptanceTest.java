package monorail.linkpay.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkCardAcceptanceTest extends AcceptanceTest{

    @Test
    void 내_지갑에서_링크카드를_생성한다(){
        String accessToken = 엑세스_토큰();
        LinkCardCreateRequest request = new LinkCardCreateRequest(
                "테스트카드", 500000, LocalDate.of(2025, 5, 25));
        ExtractableResponse<Response> response = 링크카드_생성_요청(accessToken, request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 링크카드_생성_요청(final String accessToken, final LinkCardCreateRequest request) {
        return sendPostRequest("/api/cards", accessToken, request);
    }
}
