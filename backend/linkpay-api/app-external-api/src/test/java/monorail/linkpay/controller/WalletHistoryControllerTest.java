package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.WALLET_HISTORY_LIST_RESPONSE;
import static monorail.linkpay.controller.ControllerFixture.WALLET_HISTORY_RESPONSE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class WalletHistoryControllerTest extends ControllerTest {

    @Test
    void 내_지갑_내역_목록을_확인한다() {
        when(walletHistoryService.readMyWalletHistoryPage(anyLong(), anyLong(), anyInt()))
                .thenReturn(WALLET_HISTORY_LIST_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallet-histories/my-wallet?&lastId=1&size=10")
                .then().log().all()
                .apply(document("wallethistories/read/mywallet"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크지갑_내역_목록을_확인한다() {
        when(walletHistoryService.readLinkedWalletHistoryPage(anyLong(), anyLong(), anyInt()))
                .thenReturn(WALLET_HISTORY_LIST_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallet-histories/linked-wallet?walletId=1&lastId=1&size=10")
                .then().log().all()
                .apply(document("wallethistories/read/linkedwallet"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 지갑_세부내역을_확인한다() {
        when(walletHistoryService.readWalletHistory(anyLong(), anyLong())).thenReturn(WALLET_HISTORY_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallet-histories/1")
                .then().log().all()
                .apply(document("wallethistories/read/one"))
                .statusCode(HttpStatus.OK.value());
    }
}
