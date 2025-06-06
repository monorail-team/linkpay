package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINKED_MEMBERS_RESPONSE;
import static monorail.linkpay.controller.ControllerFixture.LINKED_MEMBER_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LinkedMemberControllerTest extends ControllerTest {

    @Test
    void 링크멤버를_조회한다() {
        when(linkedMemberService.getLinkedMembers(anyLong(), anyLong(), anyLong(), anyInt()))
                .thenReturn(LINKED_MEMBERS_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets/1/members?lastId=1&size=10")
                .then().log().all()
                .apply(document("linkedmembers/read"))
                .statusCode(HttpStatus.OK.value());
    }

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
        doNothing().when(linkedMemberService).deleteLinkedMember(anyLong(), anyLong(), anyLong());

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().delete("/api/linked-wallets/1/members/1")
                .then().log().all()
                .apply(document("linkedmembers/delete/one"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 링크멤버_여러명을_삭제한다() {
        doNothing().when(linkedMemberService).deleteLinkedMembers(anyLong(), anySet(), anyLong());

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().delete("/api/linked-wallets/1/members?linkedMemberIds=1,2")
                .then().log().all()
                .apply(document("linkedmembers/delete"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
