package monorail.linkpay.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.controller.request.ChargeRequest;
import monorail.linkpay.wallet.service.WalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPatchRequest;
import static org.assertj.core.api.Assertions.assertThat;

class WalletAcceptanceTest extends AcceptanceTest {

    @Test
    void 지갑에_포인트를_충전한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 포인트_충전_요청(accessToken, new ChargeRequest(50000));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 지갑_잔액을_조회한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 지갑_잔액_조회_요청(accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(WalletResponse.class).amount()).isEqualTo(0); // todo 이부분 dinamictest로 시나리오 테스트 해주세요
    }

    public static ExtractableResponse<Response> 포인트_충전_요청(final String accessToken,
                                                             final ChargeRequest chargeRequest) {
        return sendPatchRequest("/api/wallets/charge", accessToken, chargeRequest);
    }

    public static ExtractableResponse<Response> 지갑_잔액_조회_요청(final String accessToken) {
        return sendGetRequest("/api/wallets", accessToken);
    }
}
