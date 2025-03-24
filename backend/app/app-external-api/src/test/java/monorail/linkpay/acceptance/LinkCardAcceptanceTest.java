package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.stream.Stream;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class LinkCardAcceptanceTest extends AcceptanceTest {

    @Test
    void 내_지갑에서_링크카드를_생성한다() {
        String accessToken = 엑세스_토큰();
        LinkCardCreateRequest request = new LinkCardCreateRequest(
                "테스트카드", 500000, LocalDate.now().plusMonths(1));
        ExtractableResponse<Response> response = 링크카드_생성_요청(accessToken, request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @TestFactory
    Stream<DynamicTest> 보유한_링크카드를_조회한다() {
        String accessToken = 엑세스_토큰();
        LinkCardCreateRequest request = new LinkCardCreateRequest(
                "테스트카드", 500000, LocalDate.now().plusMonths(1));

        return Stream.of(
                dynamicTest("내 지갑에서 링크카드를 생성한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_생성_요청(accessToken, request);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                }),
                dynamicTest("보유한 링크카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_조회_요청(accessToken);

                    LinkCardsResponse linkCardsResponse = response.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).hasSize(1)
                    );
                }),
                dynamicTest("생성한 카드 중 결제카드로 등록되지 않은 카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 등록되지_않은_카드_조회(accessToken);
                    LinkCardsResponse linkCardsResponse = response.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).hasSize(1)
                    );
                })
        );
    }

    private ExtractableResponse<Response> 등록되지_않은_카드_조회(String accessToken) {
        return sendGetRequest("/api/cards/unregister", accessToken);
    }

    private ExtractableResponse<Response> 링크카드_조회_요청(String accessToken) {
        return sendGetRequest("/api/cards", accessToken);
    }

    private ExtractableResponse<Response> 링크카드_생성_요청(final String accessToken, final LinkCardCreateRequest request) {
        return sendPostRequest("/api/cards", accessToken, request);
    }


}
