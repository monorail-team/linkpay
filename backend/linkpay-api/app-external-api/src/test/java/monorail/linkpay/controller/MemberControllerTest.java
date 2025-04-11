package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.MEMBER_RESPONSE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberControllerTest extends ControllerTest {

    @Test
    void 회원을_이메일로_조회한다() {
        when(memberService.getMember(anyString())).thenReturn(MEMBER_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/members?email=linked@gmail.com")
                .then().log().all()
                .apply(document("members/read"))
                .statusCode(HttpStatus.OK.value());
    }
}
