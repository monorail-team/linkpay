package monorail.linkpay.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.controller.request.StoreCreateRequest;
import monorail.linkpay.controller.request.StoreTransactionRequest;
import monorail.linkpay.store.dto.TransactionResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class StoreTransactionAcceptanceTest extends AcceptanceTest {


    @TestFactory
    Stream<DynamicTest> 거래정보_생성_및_결제_성공_시나리오() {
        String accessToken = 엑세스_토큰();
        var storeUrl = new ThreadLocal<String>();
        var transaction = new ThreadLocal<TransactionResponse>();

        return Stream.of(
                dynamicTest("가게를 생성한다.", () -> {
                    var request = new StoreCreateRequest("새로운 가게");
                    var response = 가게_생성_요청(accessToken, request);

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                    assertThat(response.header("Location")).isNotBlank();

                    storeUrl.set(response.header("Location"));
                }),
                dynamicTest("가게에서 거래 정보를 생성한다.", () -> {
                    var request = new StoreTransactionRequest(1000L);
                    var response = 거래정보_생성_요청(accessToken, storeUrl.get(), request);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

                    var transactionResponse = response.as(TransactionResponse.class);
                    assertThat(transactionResponse).isNotNull();

                    transaction.set(transactionResponse);
                }),
                dynamicTest("결제자가 거래에 대한 결제를 요청하면 결제가 완료된다.", () -> {
                    // todo
                }),
                dynamicTest("완료된 결제 정보를 조회한다.", () -> {
                    // todo
                })
        );
    }

    private ExtractableResponse<Response> 가게_생성_요청(final String accessToken, final StoreCreateRequest request) {
        return sendPostRequest("/api/stores", accessToken, request);
    }

    private ExtractableResponse<Response> 거래정보_생성_요청(final String accessToken, final String storeUrl, final StoreTransactionRequest request) {
        return sendPostRequest("%s/transactions".formatted(storeUrl), accessToken, request);
    }

}
