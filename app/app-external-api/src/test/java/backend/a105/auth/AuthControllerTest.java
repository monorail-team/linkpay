package backend.a105.auth;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @TestFactory
    Stream<DynamicTest> 로그인_및_인증_시나리오() {
        AtomicReference<String> accessToken = new AtomicReference<>();

        Response 테스트_인증_요청 = RestAssured
                .given()
                .header("Authorization", "Bearer " + accessToken.get())
                .when()
                .get("/api/auth/test-authenticate");

        Response 카카오_로그인_요청 = RestAssured
                .given()
                .param("code", "사용자가 카카오 로그인 후 발급받은 코드")
                .when()
                .post("/api/auth/login/kakao");

        return Stream.of(
                dynamicTest("로그인하지 않고 인증이 필요한 api요청 시 401응답을 반환받는다.", () -> {
                    테스트_인증_요청
                            .then()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .log().all();
                }),

                dynamicTest("카카오 로그인을 통해 엑세스 토큰을 발급받는다.", () -> {
                    AuthResponse authResponse = 카카오_로그인_요청
                            .then()
                            .statusCode(HttpStatus.OK.value())
                            .body("accessToken", notNullValue())
                            .log().all()
                            .extract().as(AuthResponse.class);

                    accessToken.set(authResponse.accessToken());
                }),

                dynamicTest("발급받은 엑세스 토큰을 사용하면 인증에 성공한다.", () -> {
                    테스트_인증_요청
                            .then()
                            .statusCode(HttpStatus.OK.value())
                            .log().all();
                })
        );
    }

}