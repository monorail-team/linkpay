package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLETS_RESPONSE;
import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLET_CREATE_REQUEST;
import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLET_RESPONSE_1;
import static monorail.linkpay.controller.ControllerFixture.WALLET_POINT_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import monorail.linkpay.common.domain.Point;
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

    @Test
    void 링크지갑_목록을_조회한다() {
        when(linkedWalletService.readLinkedWallets(anyLong(), anyLong(), anyInt())).thenReturn(LINKED_WALLETS_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets?lastId=1&size=10")
                .then().log().all()
                .apply(document("linkedwallets/read"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크지갑_단건_조회한다() {
        when(linkedWalletService.readLinkedWallet(anyLong())).thenReturn(LINKED_WALLET_RESPONSE_1);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets/1")
                .then().log().all()
                .apply(document("linkedwallets/read/one"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크지갑을_충전한다() {
        doNothing().when(linkedWalletService).chargeLinkedWallet(anyLong(), any(Point.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WALLET_POINT_REQUEST)
                .when().patch("/api/linked-wallets/charge/1")
                .then().log().all()
                .apply(document("linkedwallets/charge"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 링크지갑의_잔액을_차감한다() {
        doNothing().when(linkedWalletService).deductLinkedWallet(anyLong(), any(Point.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WALLET_POINT_REQUEST)
                .when().patch("/api/linked-wallets/deduct/1")
                .then().log().all()
                .apply(document("linkedwallets/deduct"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
