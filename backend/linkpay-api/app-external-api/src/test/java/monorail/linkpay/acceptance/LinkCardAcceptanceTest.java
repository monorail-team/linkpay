package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.LinkedWalletAcceptanceTest.링크지갑_목록_조회_요청;
import static monorail.linkpay.acceptance.LinkedWalletAcceptanceTest.링크지갑_생성_요청;
import static monorail.linkpay.acceptance.MemberAcceptanceTest.회원_조회_요청;
import static monorail.linkpay.acceptance.MyWalletAcceptanceTest.포인트_충전_요청;
import static monorail.linkpay.acceptance.PaymentAcceptanceTest.결제_요청;
import static monorail.linkpay.acceptance.StoreTransactionAcceptanceTest.가게_생성_요청;
import static monorail.linkpay.acceptance.StoreTransactionAcceptanceTest.거래정보_생성_요청;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendDeleteRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPatchRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.controller.request.LinkCardRegistRequest;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.PaymentsRequest;
import monorail.linkpay.controller.request.SharedLinkCardCreateRequest;
import monorail.linkpay.controller.request.StoreCreateRequest;
import monorail.linkpay.controller.request.StoreTransactionRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.linkcard.dto.LinkCardDetailResponse;
import monorail.linkpay.linkcard.dto.LinkCardHistoriesResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.member.dto.MemberResponse;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class LinkCardAcceptanceTest extends AcceptanceTest {


    public static final String EMAIL = "email@kakao.com";

    @Test
    void 내_지갑에서_링크카드를_생성한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 내_지갑_링크카드를_상세조회한다() {
        String accessToken = 엑세스_토큰();
        링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);
        ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "owned");
        String linkCardId = cardRes.as(LinkCardsResponse.class).linkCards().getFirst().linkCardId();
        ExtractableResponse<Response> cardDetailRes = 링크카드_상세_조회_요청(accessToken,
                Long.parseLong(linkCardId));
        LinkCardDetailResponse cardDetailResponse = cardDetailRes.as(LinkCardDetailResponse.class);

        assertThat(linkCardId).isEqualTo(cardDetailResponse.linkCardId());
    }

    @Test
    void 링크지갑에서_링크카드를_생성한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken,
                new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
        ExtractableResponse<Response> walletResponse = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
        LinkedWalletsResponse linkedWalletsResponse = walletResponse.as(LinkedWalletsResponse.class);

        ExtractableResponse<Response> memberRes = 회원_조회_요청(accessToken, EMAIL);
        MemberResponse memberResponse = memberRes.as(MemberResponse.class);

        SharedLinkCardCreateRequest sharedLinkCardCreateRequest = new SharedLinkCardCreateRequest(
                "테스트카드", 500000, LocalDate.now().plusMonths(1), List.of(memberResponse.memberId(), "1"),
                linkedWalletsResponse.linkedWallets().getFirst().linkedWalletId());

        ExtractableResponse<Response> response = 링크지갑에서_링크카드_생성_요청(accessToken, sharedLinkCardCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @TestFactory
    Stream<DynamicTest> 다양한_링크카드를_삭제하고_확인한다() {
        String accessToken = 엑세스_토큰();
        String linkedWalletName = "링크지갑1";

        return Stream.of(
                dynamicTest("내 지갑에서 링크카드 생성후 삭제한다", () -> {
                    링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "owned");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);

                    ExtractableResponse<Response> response = 링크카드_삭제_요청(accessToken,
                            linkCardsResponse.linkCards().getFirst().linkCardId());

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("내 지갑 링크카드가 삭제된 것을 확인한다", () -> {
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "owned");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);
                    assertThat(linkCardsResponse.linkCards()).isEmpty();
                }),

                dynamicTest("링크지갑에서 링크카드 생성한다", () -> {
                    링크지갑_생성_요청(accessToken,
                            new LinkedWalletCreateRequest(linkedWalletName, Set.of("1", "2", "3")));
                    ExtractableResponse<Response> walletResponse = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
                    LinkedWalletsResponse linkedWalletsResponse = walletResponse.as(LinkedWalletsResponse.class);

                    ExtractableResponse<Response> memberRes = 회원_조회_요청(accessToken, EMAIL);
                    MemberResponse memberResponse = memberRes.as(MemberResponse.class);

                    SharedLinkCardCreateRequest sharedLinkCardCreateRequest = new SharedLinkCardCreateRequest(
                            "테스트카드", 500000, LocalDate.now().plusMonths(1),
                            List.of(memberResponse.memberId(), "1"),
                            linkedWalletsResponse.linkedWallets().getFirst().linkedWalletId());

                    링크지갑에서_링크카드_생성_요청(accessToken, sharedLinkCardCreateRequest);

                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "linked");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);

                    assertThat(linkCardsResponse.linkCards()).hasSize(1);
                }),

                dynamicTest("링크지갑 링크카드_상세조회한다", () -> {
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "linked");
                    String linkCardId = cardRes.as(LinkCardsResponse.class).linkCards().getFirst().linkCardId();

                    ExtractableResponse<Response> cardDetailRes = 링크카드_상세_조회_요청(accessToken,
                            Long.parseLong(linkCardId));
                    LinkCardDetailResponse cardDetailResponse = cardDetailRes.as(LinkCardDetailResponse.class);
                    assertAll(
                            () -> assertThat(linkCardId).isEqualTo(cardDetailResponse.linkCardId()),
                            () -> assertThat(cardDetailResponse.walletName()).isEqualTo(linkedWalletName)
                    );
                }),

                dynamicTest("링크지갑 카드 중 내 카드를 삭제한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_삭제_요청(accessToken,
                            링크카드_조회_요청(accessToken, "linked").as(LinkCardsResponse.class)
                                    .linkCards().getFirst().linkCardId());

                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "linked");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).isEmpty()
                    );
                }),

                dynamicTest("링크지갑 카드 중 공유한 카드를 조회한다", () -> {
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "shared");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);

                    assertThat(linkCardsResponse.linkCards()).hasSize(1);
                }),

                dynamicTest("공유한 링크카드를 삭제한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_삭제_요청(accessToken,
                            링크카드_조회_요청(accessToken, "shared").as(LinkCardsResponse.class)
                                    .linkCards().getFirst().linkCardId());

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("공유한 링크카드를 조회한다", () -> {
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "shared");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);
                    assertThat(linkCardsResponse.linkCards()).isEmpty();
                })
        );
    }

    @TestFactory
    Stream<DynamicTest> 보유한_링크카드를_조회하고_결제카드로_등록한다() {
        String accessToken = 엑세스_토큰();
        AtomicReference<LinkCardsResponse> unregisteredLinkCardsResponse = new AtomicReference<>();

        return Stream.of(
                dynamicTest("내 지갑에서 링크카드를 생성한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                }),

                dynamicTest("링크 지갑에서 링크카드를 생성한다", () -> {
                    링크지갑_생성_요청(accessToken,
                            new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
                    ExtractableResponse<Response> walletResponse = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
                    LinkedWalletsResponse linkedWalletsResponse = walletResponse.as(LinkedWalletsResponse.class);

                    ExtractableResponse<Response> memberRes = 회원_조회_요청(accessToken, EMAIL);
                    MemberResponse memberResponse = memberRes.as(MemberResponse.class);

                    SharedLinkCardCreateRequest sharedLinkCardCreateRequest = new SharedLinkCardCreateRequest(
                            "테스트카드", 500000, LocalDate.now().plusMonths(1),
                            List.of(memberResponse.memberId(), "1", "2", "3"),
                            linkedWalletsResponse.linkedWallets().getFirst().linkedWalletId());

                    ExtractableResponse<Response> response = 링크지갑에서_링크카드_생성_요청(accessToken,
                            sharedLinkCardCreateRequest);
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                }),

                dynamicTest("보유한 내 지갑 링크카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_조회_요청(accessToken, "owned");

                    LinkCardsResponse linkCardsResponse = response.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).hasSize(1)
                    );
                }),

                dynamicTest("보유한 링크지갑 링크카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_조회_요청(accessToken, "linked");

                    LinkCardsResponse linkCardsResponse = response.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).hasSize(1)
                    );
                }),

                dynamicTest("공유한 링크카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_조회_요청(accessToken, "shared");

                    LinkCardsResponse linkCardsResponse = response.as(LinkCardsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkCardsResponse.linkCards()).hasSize(3)
                    );
                }),

                dynamicTest("생성한 카드 중 결제카드로 등록되지 않은 카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 등록되지_않은_카드_조회(accessToken);
                    unregisteredLinkCardsResponse.set(response.as(LinkCardsResponse.class));
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(unregisteredLinkCardsResponse.get().linkCards()).hasSize(2)
                    );
                }),

                dynamicTest("링크카드를 결제카드로 등록한다", () -> {
                    String cardId =
                            unregisteredLinkCardsResponse.get().linkCards().getFirst().linkCardId();
                    ExtractableResponse<Response> response = 카드_등록(accessToken,
                            new LinkCardRegistRequest(List.of(cardId)));
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("생성한 카드 중 결제카드로 등록된 카드를 조회한다", () -> {
                    ExtractableResponse<Response> response = 등록된_카드_조회(accessToken);
                    unregisteredLinkCardsResponse.set(response.as(LinkCardsResponse.class));
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(unregisteredLinkCardsResponse.get().linkCards()).hasSize(1)
                    );
                })
        );
    }

    @TestFactory
    Stream<DynamicTest> 링크카드로_결제하고_내역을_조회한다() {
        String accessToken = 엑세스_토큰();
        AtomicReference<Long> linkCardId = new AtomicReference<>();

        return Stream.of(
                dynamicTest("링크카드 생성 및 등록", () -> {
                    링크카드_생성_요청(accessToken, LINK_CARD_CREATE_REQUEST);
                    ExtractableResponse<Response> cardRes = 링크카드_조회_요청(accessToken, "owned");
                    LinkCardsResponse linkCardsResponse = cardRes.as(LinkCardsResponse.class);
                    linkCardId.set(Long.parseLong(linkCardsResponse.linkCards().getFirst().linkCardId()));
                    assertThat(linkCardsResponse.linkCards()).hasSize(1);
                }),
                dynamicTest("지갑 충전 및 카드 사용", () -> {
                    포인트_충전_요청(accessToken, new WalletPointRequest(50000));

                    var storeRes = 가게_생성_요청(accessToken, new StoreCreateRequest("새로운 가게"));
                    String storeId = storeRes.header("Location").split("/")[3];
                    var payInfoRes = 거래정보_생성_요청(accessToken, storeRes.header("Location"),
                            new StoreTransactionRequest(3000L));
                    TransactionResponse transactionResponse = payInfoRes.as(TransactionResponse.class);
                    ExtractableResponse<Response> response = 결제_요청(accessToken,
                            new PaymentsRequest(3000L,
                                    linkCardId.get().toString(),
                                    storeId, transactionResponse.transactionSignature(),
                                    "token"));
                }),
                dynamicTest("카드 내역을 확인한다", () -> {
                    ExtractableResponse<Response> response = 링크카드_사용내역_조회_요청(accessToken, linkCardId.get());
                    LinkCardHistoriesResponse histories = response.as(LinkCardHistoriesResponse.class);
                    assertAll(
                            () -> assertThat(histories.linkCardHistories()).hasSize(1),
                            () -> assertThat(histories.linkCardHistories().getFirst().linkCardId()).isEqualTo(
                                    linkCardId.get().toString())
                    );
                })
        );
    }

    private ExtractableResponse<Response> 카드_등록(final String accessToken,
                                                final LinkCardRegistRequest linkCardRegistRequest) {
        return sendPatchRequest("api/cards/activate", accessToken, linkCardRegistRequest);
    }

    private ExtractableResponse<Response> 등록되지_않은_카드_조회(final String accessToken) {
        return sendGetRequest("/api/cards/unregistered", accessToken);
    }

    private ExtractableResponse<Response> 등록된_카드_조회(final String accessToken) {
        return sendGetRequest("/api/cards/registered", accessToken);
    }

    public static ExtractableResponse<Response> 링크카드_조회_요청(final String accessToken, final String type) {
        return sendGetRequest("/api/cards?type=" + type, accessToken);
    }

    public static ExtractableResponse<Response> 링크카드_생성_요청(final String accessToken,
                                                           final LinkCardCreateRequest request) {
        return sendPostRequest("/api/cards", accessToken, request);
    }

    private ExtractableResponse<Response> 링크지갑에서_링크카드_생성_요청(final String accessToken,
                                                            final SharedLinkCardCreateRequest request) {
        return sendPostRequest("/api/cards/shared", accessToken, request);
    }

    private ExtractableResponse<Response> 링크카드_삭제_요청(final String accessToken, final String linkCardId) {
        return sendDeleteRequest("/api/cards?linkCardId=" + linkCardId, accessToken);
    }

    private ExtractableResponse<Response> 링크카드_상세_조회_요청(final String accessToken, final Long linkCardId) {
        return sendGetRequest("/api/cards/detail?linkCardId=" + linkCardId, accessToken);
    }

    private ExtractableResponse<Response> 링크카드_사용내역_조회_요청(final String accessToken, final Long linkCardId) {
        return sendGetRequest("/api/cards/card-histories/" + linkCardId, accessToken);
    }
}
