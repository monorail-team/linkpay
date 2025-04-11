package monorail.linkpay.controller;

import monorail.linkpay.controller.request.FcmRegisterRequest;
import monorail.linkpay.fcm.service.dto.FcmRegisterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static monorail.linkpay.controller.ControllerFixture.SHARED_LINK_CARD_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

public class FcmControllerTest extends ControllerTest {

    @Test
    void 토큰을_등록한다() {
        when(fcmService.register(anyLong(), anyString(), anyString()))
                .thenReturn(new FcmRegisterResponse(Instant.now()));

        FcmRegisterRequest request = FcmRegisterRequest.builder()
                .token("token")
                .deviceId("uuid")
                .build();
        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .header("content-type", "application/json")
                .body(request)
                .when().put("api/fcm/register")
                .then().log().all()
                .apply(document("fcm/register"))
                .statusCode(HttpStatus.OK.value());

    }
}
