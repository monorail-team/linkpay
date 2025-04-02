package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.LinkCardAcceptanceTest.링크카드_생성_요청;
import static monorail.linkpay.acceptance.LinkCardAcceptanceTest.링크카드_조회_요청;
import static monorail.linkpay.acceptance.MyWalletAcceptanceTest.지갑_잔액_조회_요청;
import static monorail.linkpay.acceptance.MyWalletAcceptanceTest.포인트_충전_요청;
import static monorail.linkpay.acceptance.PaymentAcceptanceTest.결제_요청;
import static monorail.linkpay.acceptance.StoreTransactionAcceptanceTest.가게_생성_요청;
import static monorail.linkpay.acceptance.StoreTransactionAcceptanceTest.거래정보_생성_요청;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import monorail.linkpay.controller.request.PaymentsRequest;
import monorail.linkpay.controller.request.StoreCreateRequest;
import monorail.linkpay.controller.request.StoreTransactionRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.wallet.dto.WalletResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class WalletHistoryAcceptanceTest extends AcceptanceTest {

    @TestFactory
    Stream<DynamicTest> 내_지갑_카드사용_후_지갑_내역_확인한다() {
        String accessToken = 엑세스_토큰();
        var transaction = new ThreadLocal<TransactionResponse>();

        return Stream.of(
                dynamicTest("내 지갑에 충전후 잔액을 확인한다", () -> {
                    포인트_충전_요청(accessToken, new WalletPointRequest(50000));
                    ExtractableResponse<Response> response = 지갑_잔액_조회_요청(accessToken);

                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.as(WalletResponse.class).amount()).isEqualTo(50000)
                    );
                }),
                dynamicTest("지갑 히스토리에서 충전 내역을 확인한다", () -> {
                    ExtractableResponse<Response> walletHistoriesRes = 나의_지갑_히스토리_리스트_조회_요청(accessToken);
                    WalletHistoryListResponse walletHistories = walletHistoriesRes.as(WalletHistoryListResponse.class);
                    ExtractableResponse<Response> response = 지갑_히스토리_조회_요청(accessToken,
                            Long.parseLong(walletHistories.walletHistories().getFirst().walletHistoryId()));

                    WalletHistoryResponse walletHistoryResponse = response.as(WalletHistoryResponse.class);
                    assertAll(
                            () -> assertThat(walletHistoryResponse.remaining()).isEqualTo(50000),
                            () -> assertThat(walletHistoryResponse.linkCardId()).isNull()
                    );
                }),
                dynamicTest("링크카드 생성 후 결제한다", () -> {
                    링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);
                    LinkCardsResponse cardsResponse = 링크카드_조회_요청(accessToken, "owned").as(LinkCardsResponse.class);
                    var storeRes = 가게_생성_요청(accessToken, new StoreCreateRequest("새로운 가게"));
                    String storeId = storeRes.header("Location").split("/")[3];
                    var payInfoRes = 거래정보_생성_요청(accessToken, storeRes.header("Location"),
                            new StoreTransactionRequest(3000L));
                    TransactionResponse transactionResponse = payInfoRes.as(TransactionResponse.class);
                    transaction.set(transactionResponse);
                    ExtractableResponse<Response> response = 결제_요청(accessToken,
                            new PaymentsRequest(3000L,
                                    Long.parseLong(cardsResponse.linkCards().getFirst().linkCardId()),
                                    Long.parseLong(storeId), transactionResponse.transactionSignature(),
                                    "token"));

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                }),
                dynamicTest("지갑 히스토리에서 결제 내역을 확인한다", () -> {
                    ExtractableResponse<Response> walletHistoriesRes = 나의_지갑_히스토리_리스트_조회_요청(accessToken);
                    WalletHistoryListResponse walletHistories = walletHistoriesRes.as(WalletHistoryListResponse.class);
                    ExtractableResponse<Response> response = 지갑_히스토리_조회_요청(accessToken,
                            Long.parseLong(walletHistories.walletHistories().getFirst().walletHistoryId()));

                    WalletHistoryResponse walletHistoryResponse = response.as(WalletHistoryResponse.class);
                    assertAll(
                            () -> assertThat(walletHistoryResponse.amount()).isEqualTo(3000),
                            () -> assertThat(
                                    walletHistoryResponse.linkCardName()).isEqualTo(LINK_CARD_CREATE_REQUEST.cardName())
                    );

                })
        );
    }

    private ExtractableResponse<Response> 지갑_히스토리_조회_요청(final String accessToken, final Long walletHistoryId) {
        return sendGetRequest("/api/wallet-histories/" + walletHistoryId, accessToken);
    }

    private ExtractableResponse<Response> 나의_지갑_히스토리_리스트_조회_요청(final String accessToken) {
        return sendGetRequest("/api/wallet-histories/my-wallet", accessToken);
    }
}
