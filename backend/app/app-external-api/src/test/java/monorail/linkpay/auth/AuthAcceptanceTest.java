package monorail.linkpay.auth;

import monorail.linkpay.ClientTestConfiguration;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.kakao.KakaoOauthClient;
import monorail.linkpay.kakao.dto.KakaoOauthResponse;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberService;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

@Slf4j
@Import(ClientTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthAcceptanceTest {

    @Autowired
    KakaoOauthClient mockKakaoOauthClient;

    @Autowired
    MemberService memberService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @TestFactory
    Stream<DynamicTest> 카카오_로그인_및_인증_시나리오() {
        AtomicReference<String> accessToken = new AtomicReference<>("");
        String kakaoOauthCode = "kakaocode";
        String kakaoOauthAccessToken = "kaccesstoken";
        Member member = Member.builder()
                .id(1L)
                .email("email@kakao.com")
                .build();
        memberService.create(member);

        when(mockKakaoOauthClient.authorize(kakaoOauthCode))
                .thenReturn(ResponseEntity.ok(KakaoOauthResponse.of(kakaoOauthAccessToken)));
        when(mockKakaoOauthClient.fetchUser(kakaoOauthAccessToken))
                .thenReturn(ResponseEntity.ok(KakaoUserResponse.of(member.getEmail())));

        return Stream.of(
                dynamicTest("로그인하지 않고 인증이 필요한 api요청 시 401응답을 반환받는다.", () -> {
                    RestAssured
                            .given()
                            .when()
                            .get("/api/auth/test-authenticate")
                            .then()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .log().all();
                }),

                dynamicTest("카카오 로그인을 통해 엑세스 토큰을 발급받는다.", () -> {
                    var response = RestAssured
                            .given()
                            .param("code", kakaoOauthCode)
                            .when()
                            .post("/api/auth/login/kakao")
                            .then()
                            .statusCode(HttpStatus.OK.value())
                            .body("accessToken", notNullValue())
                            .log().all()
                            .extract().as(LoginResponse.class);

                    accessToken.set(response.accessToken());
                }),

                dynamicTest("발급받은 엑세스 토큰을 사용하면 인증에 성공한다.", () -> {
                    RestAssured
                            .given()
                            .header("Authorization", "Bearer " + accessToken.get())
                            .when()
                            .get("/api/auth/test-authenticate")
                            .then()
                            .statusCode(HttpStatus.OK.value())
                            .log().all();
                })
        );
    }

}