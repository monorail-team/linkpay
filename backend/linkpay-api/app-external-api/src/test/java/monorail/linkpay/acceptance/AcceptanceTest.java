package monorail.linkpay.acceptance;

import io.restassured.RestAssured;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.service.PaymentTokenProvider;
import monorail.linkpay.wallet.client.BankAccountClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Sql(value = {
        "/data/truncate.sql",
        "/data/member.sql",
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AcceptanceTest {

    public static final String KAKAO_OAUTH_CODE = "kakaocode";
    public static final String KAKAO_OAUTH_ACCESS_TOKEN = "kaccesstoken";
    public static final LinkCardCreateRequest LINK_CARD_CREATE_REQUEST = new LinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.now().plusMonths(1));

    @LocalServerPort
    int port;
    @MockitoBean
    protected KakaoOauthClient mockKakaoOauthClient;
    @MockitoBean
    protected PaymentTokenProvider mockPaymentTokenProvider;
    @MockitoBean
    protected BankAccountClient bankAccountClient;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        setUpKakaoOauthMock();
        setUpPaymentTokenMock();
    }

    private void setUpKakaoOauthMock() {
        when(mockKakaoOauthClient.authorize(KAKAO_OAUTH_CODE))
                .thenReturn(ResponseEntity.ok(KakaoOauthResponse.of(KAKAO_OAUTH_ACCESS_TOKEN)));
        when(mockKakaoOauthClient.fetchUser(KAKAO_OAUTH_ACCESS_TOKEN))
                .thenReturn(ResponseEntity.ok(KakaoUserResponse.of("email@kakao.com", "username")));
    }

    // TODO: WebAuthn 로직 완성되면 실제 요청으로 대체
    private void setUpPaymentTokenMock(){
        when(mockPaymentTokenProvider.generateFor(anyLong()))
                .thenReturn("mockPaymentToken");
        doNothing().when(mockPaymentTokenProvider)
                .validate(any(LinkCard.class), anyString());
    }
}
