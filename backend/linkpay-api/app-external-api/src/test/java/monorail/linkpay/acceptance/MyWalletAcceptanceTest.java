package monorail.linkpay.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.wallet.dto.WalletResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPatchRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class MyWalletAcceptanceTest extends AcceptanceTest {

    @TestFactory
    public Stream<DynamicTest> 지갑_포인트_충전과_잔액_조회_시나리오() {
        final String accessToken = 엑세스_토큰();

        return Stream.of(
                dynamicTest("지갑에 포인트를 충전한다", () -> {
                    ExtractableResponse<Response> response = 포인트_충전_요청(accessToken, new WalletPointRequest(50000));
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),
                dynamicTest("충전 후 지갑 잔액을 조회한다", () -> {
                    ExtractableResponse<Response> response = 지갑_잔액_조회_요청(accessToken);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.as(WalletResponse.class).amount()).isEqualTo(50000);
                })
        );
    }

    public static ExtractableResponse<Response> 포인트_충전_요청(final String accessToken,
                                                          final WalletPointRequest pointRequest) {
        return sendPatchRequest("/api/my-wallets/charge", accessToken, pointRequest);
    }

    public static ExtractableResponse<Response> 지갑_잔액_조회_요청(final String accessToken) {
        return sendGetRequest("/api/my-wallets", accessToken);
    }
}
