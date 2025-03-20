package monorail.linkpay.acceptance.client;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RestAssuredClient {

    public static ExtractableResponse<Response> sendGetRequest(final String url, final String accessToken) {
        return RestAssured
                .given().header("Authorization", "Bearer " + accessToken)
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
