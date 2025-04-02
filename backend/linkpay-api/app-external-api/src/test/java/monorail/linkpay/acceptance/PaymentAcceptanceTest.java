package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.controller.request.PaymentsRequest;

public class PaymentAcceptanceTest extends AcceptanceTest {

    public static ExtractableResponse<Response> 결제_요청(final String accessToken, final PaymentsRequest paymentsRequest) {
        return sendPostRequest("/api/payments", accessToken, paymentsRequest);
    }
}
