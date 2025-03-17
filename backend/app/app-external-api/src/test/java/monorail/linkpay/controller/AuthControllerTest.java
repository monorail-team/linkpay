package monorail.linkpay.controller;

import monorail.linkpay.auth.dto.KakaoLoginRequest;
import monorail.linkpay.auth.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

public class AuthControllerTest extends ControllerTest {

    @Test
    void 로그인한다() {
        when(authService.login(any(KakaoLoginRequest.class))).thenReturn(new LoginResponse("accessToken"));

        docsGiven
            .when().post("api/auth/login/kakao?code=1")
            .then().log().all()
            .apply(document("auth/login/kakao",
                queryParameters(
                    parameterWithName("code").description("인가 코드")
                ),
                responseFields(
                    fieldWithPath("accessToken").description("엑세스 토큰")
                )))
            .statusCode(HttpStatus.OK.value());
    }
}
