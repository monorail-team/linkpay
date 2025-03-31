package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import monorail.linkpay.member.dto.MemberResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@kakao.com";

    @Test
    void 이메일을_통해_회원을_조회한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 회원_조회_요청(accessToken, EMAIL);

        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberResponse.email()).isEqualTo("email@kakao.com")
        );
    }

    public static ExtractableResponse<Response> 회원_조회_요청(final String accessToken, final String email) {
        return sendGetRequest("/api/members?email=%s".formatted(email), accessToken);
    }
}
