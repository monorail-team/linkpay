package monorail.linkpay.acceptance.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RestAssuredClient {

    public static ExtractableResponse<Response> sendGetRequest(final String url) {
        return RestAssured
                .given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendGetRequest(final String url,
                                                               final String accessToken) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPostRequest(final String url) {
        return RestAssured
                .given().log().all()
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPostRequest(final String url,
                                                                final String accessToken,
                                                                final Object requestBody) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendPatchRequest(final String url,
                                                                 final String accessToken,
                                                                 final Object requestBody) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().patch(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendDeleteRequest(final String url,
                                                                  final String accessToken) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
