package monorail.linkpay.acceptance;

import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

//@Sql(value = {
//    "/data/truncate.sql",
//    "/data/member.sql",
//    "/data/wallet.sql"
//})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    public static final String KAKAO_OAUTH_CODE = "kakaocode";
    public static final String KAKAO_OAUTH_ACCESS_TOKEN = "kaccesstoken";

    @LocalServerPort
    int port;
    @MockitoBean
    protected KakaoOauthClient mockKakaoOauthClient;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        setUpKakaoOauthMock();
    }

    private void setUpKakaoOauthMock() {
        when(mockKakaoOauthClient.authorize(KAKAO_OAUTH_CODE))
                .thenReturn(ResponseEntity.ok(KakaoOauthResponse.of(KAKAO_OAUTH_ACCESS_TOKEN)));
        when(mockKakaoOauthClient.fetchUser(KAKAO_OAUTH_ACCESS_TOKEN))
                .thenReturn(ResponseEntity.ok(KakaoUserResponse.of("email@kakao.com")));
    }
}
