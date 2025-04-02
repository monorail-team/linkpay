package monorail.linkpay.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static monorail.linkpay.controller.ControllerFixture.MEMBER_RESPONSE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class MypageControllerTest extends ControllerTest {

    @Test
    void 내_정보를_조회한다() {
        when(memberService.getMember(anyLong())).thenReturn(MEMBER_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/mypage")
                .then().log().all()
                .apply(document("mypage/read"))
                .statusCode(HttpStatus.OK.value());
    }
}
