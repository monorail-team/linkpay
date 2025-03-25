package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINKED_MEMBER_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LinkedMemberControllerTest extends ControllerTest {

    @Test
    void 링크멤버를_추가한다() {
        doNothing().when(linkedMemberService).createLinkedMember(anyLong(), anyLong(), anyLong());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(LINKED_MEMBER_CREATE_REQUEST)
                .when().post("/api/linked-wallets/1/members")
                .then().log().all()
                .apply(document("linkedmembers/create"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 링크멤버를_삭제한다() {
        doNothing().when(linkedMemberService)
                .deleteLinkedMember(anyLong(), anySet(), anyLong(), any(LocalDateTime.class));

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().delete("/api/linked-wallets/1/members?linkedMemberIds=1,2")
                .then().log().all()
                .apply(document("linkedmembers/delete"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
