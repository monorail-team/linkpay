package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLET_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LinkedWalletControllerTest extends ControllerTest {

    @Test
    void 링크지갑을_생성한다() {
        when(linkedWalletService.createLinkedWallet(anyLong(), anyString(), anySet())).thenReturn(1L);

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(LINKED_WALLET_CREATE_REQUEST)
                .when().post("/api/linked-wallets")
                .then().log().all()
                .apply(document("linkedwallets/create"))
                .statusCode(HttpStatus.CREATED.value());
    }
}
