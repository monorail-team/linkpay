package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLETS_RESPONSE;
import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLET_CREATE_REQUEST;
import static monorail.linkpay.controller.ControllerFixture.LINKED_WALLET_RESPONSE_1;
import static monorail.linkpay.controller.ControllerFixture.WALLET_CHANGE_REQUEST;
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
import monorail.linkpay.wallet.domain.Role;
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
    void 내가_소유한_링크지갑_목록을_조회한다() {
        when(linkedWalletService.readLinkedWallets(anyLong(), any(Role.class), anyLong(), anyInt()))
                .thenReturn(LINKED_WALLETS_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets?lastId=1&size=10&role=CREATOR")
                .then().log().all()
                .apply(document("linkedwallets/read/owned"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 내가_참여한_링크지갑_목록을_조회한다() {
        when(linkedWalletService.readLinkedWallets(anyLong(), any(Role.class), anyLong(), anyInt()))
                .thenReturn(LINKED_WALLETS_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets?lastId=1&size=10&role=PARTICIPANT")
                .then().log().all()
                .apply(document("linkedwallets/read/participant"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크지갑_단건_조회한다() {
        when(linkedWalletService.readLinkedWallet(anyLong(), anyLong())).thenReturn(LINKED_WALLET_RESPONSE_1);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/linked-wallets/1")
                .then().log().all()
                .apply(document("linkedwallets/read/one"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크지갑을_충전한다() {
        doNothing().when(linkedWalletService).chargeLinkedWallet(anyLong(), any(Point.class), anyLong());

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
    void 링크지갑을_삭제한다() {
        doNothing().when(linkedWalletService).deleteLinkedWallet(anyLong(), anyLong());

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().delete("/api/linked-wallets/1")
                .then().log().all()
                .apply(document("linkedwallets/delete"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 링크지갑의_이름을_변경한다() {
        doNothing().when(linkedWalletService).changeLinkedWallet(anyLong(), anyString(), anyLong());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WALLET_CHANGE_REQUEST)
                .when().patch("/api/linked-wallets/1")
                .then().log().all()
                .apply(document("linkedwallets/change"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
