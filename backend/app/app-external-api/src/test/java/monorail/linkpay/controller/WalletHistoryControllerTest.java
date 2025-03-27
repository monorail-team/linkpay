package monorail.linkpay.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static monorail.linkpay.controller.ControllerFixture.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class WalletHistoryControllerTest extends ControllerTest {

    @Test
    void 지갑_내역_목록을_확인한다() {
        when(walletHistoryService.readPage(anyLong(), anyLong(), anyInt())).thenReturn(WALLET_HISTORY_LIST_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallet-histories?walletId=1&lastId=1&size=10")
                .then().log().all()
                .apply(document("wallethistories/read"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 지갑_세부내역을_확인한다() {
        when(walletHistoryService.read(anyLong())).thenReturn(WALLET_HISTORY_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallet-histories/1")
                .then().log().all()
                .apply(document("wallethistories/read/one"))
                .statusCode(HttpStatus.OK.value());
    }
}
