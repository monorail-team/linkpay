package monorail.linkpay.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

public class FcmControllerTest extends ControllerTest {

    @Test
    void 토큰을_등록한다() {
        doNothing().when(fcmService).register(anyLong(), anyString());

        docsGiven
                .when().post("api/fcm/register?token=fcm-token")
                .then().log().all()
                .apply(document("fcm/register",
                        queryParameters(
                                parameterWithName("token").description("사용자 디바이스에서 생성된 FCM 토큰")
                        )))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
