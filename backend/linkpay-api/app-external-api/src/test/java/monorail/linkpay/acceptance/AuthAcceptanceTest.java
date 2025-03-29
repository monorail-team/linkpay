package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.auth.dto.LoginResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

@Slf4j
class AuthAcceptanceTest extends AcceptanceTest {

    @TestFactory
    Stream<DynamicTest> 카카오_로그인_및_인증_시나리오() {
        final AtomicReference<String> accessToken = new AtomicReference<String>();

        return Stream.of(
                dynamicTest("로그인하지 않고 인증이 필요한 api요청 시 401응답을 반환받는다.", () -> {
                    ExtractableResponse<Response> response = 인증이_필요한_요청(null);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                }),

                dynamicTest("카카오 로그인을 통해 엑세스 토큰을 발급받는다.", () -> {
                    ExtractableResponse<Response> response = 카카오_로그인(KAKAO_OAUTH_CODE);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

                    accessToken.set(response.as(LoginResponse.class).accessToken());
                    assertThat(accessToken.get()).isNotNull();
                }),

                dynamicTest("발급받은 엑세스 토큰을 사용하면 인증에 성공한다.", () -> {
                    ExtractableResponse<Response> response = 인증이_필요한_요청(accessToken.get());
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                })
        );
    }

    private static ExtractableResponse<Response> 인증이_필요한_요청(String accessToken) {
        return sendGetRequest("/api/auth/test-authenticate", accessToken);
    }

    private static ExtractableResponse<Response> 카카오_로그인(final String kakaoOauthCode) {
        return sendPostRequest("api/auth/login/kakao?code=" + kakaoOauthCode);
    }

    public static String 엑세스_토큰() {
        return 카카오_로그인(KAKAO_OAUTH_CODE)
                .as(LoginResponse.class)
                .accessToken();
    }
}