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
        ExtractableResponse<Response> response = 포인트_충전_요청(1L, accessToken, new ChargeRequest(50000));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 지갑_잔액을_조회한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 지갑_잔액_조회_요청(1L, accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(WalletResponse.class).amount()).isEqualTo(10000);
    }

    public static ExtractableResponse<Response> 포인트_충전_요청(final Long walletId, final String accessToken,
                                                             final ChargeRequest chargeRequest) {
        return sendPatchRequest("/api/wallet/%s/charge".formatted(walletId), accessToken, chargeRequest);
    }

    public static ExtractableResponse<Response> 지갑_잔액_조회_요청(final Long walletId, final String accessToken) {
        return sendGetRequest("/api/wallet/%s/charge".formatted(walletId), accessToken);
    }
}
