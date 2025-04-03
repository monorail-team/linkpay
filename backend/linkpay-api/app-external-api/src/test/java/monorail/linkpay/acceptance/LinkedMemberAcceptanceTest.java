package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.LinkedWalletAcceptanceTest.링크지갑_생성_요청;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendDeleteRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Set;
import java.util.stream.Stream;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.wallet.dto.LinkedMembersResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class LinkedMemberAcceptanceTest extends AcceptanceTest {

    @TestFactory
    Stream<DynamicTest> 링크지갑_참여자_추가_삭제_조회_시나리오() {
        String accessToken = 엑세스_토큰();
        String location = 링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of("1"))).header(
                "Location");
        Long linkedWalletId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        return Stream.of(
                dynamicTest("링크지갑에 참여자를 추가한다", () -> {
                    ExtractableResponse<Response> response = 링크지갑_참여자_추가_요청(
                            accessToken, linkedWalletId, new LinkedMemberCreateRequest("2"));

                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                }),

                dynamicTest("링크지갑의 참여자들을 모두 조회한다", () -> {
                    링크지갑_참여자_추가_요청(accessToken, linkedWalletId, new LinkedMemberCreateRequest("3"));

                    ExtractableResponse<Response> response = 링크지갑_참여자_조회_요청(accessToken, linkedWalletId);
                    LinkedMembersResponse linkedMembersResponse = response.as(LinkedMembersResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkedMembersResponse.linkedMembers()).hasSize(4)
                    );
                }),

                dynamicTest("링크지갑 참여자를 삭제한다", () -> {
                    String linkedMemberId = 링크지갑_참여자_조회_요청(accessToken, linkedWalletId).as(LinkedMembersResponse.class)
                            .linkedMembers().getLast().linkedMemberId();

                    ExtractableResponse<Response> response = 링크지갑_참여자_삭제_요청(
                            accessToken, linkedWalletId, Long.parseLong(linkedMemberId));
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("링크지갑의 참여자들을 모두 조회한다", () -> {
                    ExtractableResponse<Response> response = 링크지갑_참여자_조회_요청(accessToken, linkedWalletId);
                    LinkedMembersResponse linkedMembersResponse = response.as(LinkedMembersResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkedMembersResponse.linkedMembers()).hasSize(3)
                    );
                })
        );
    }

    private ExtractableResponse<Response> 링크지갑_참여자_추가_요청(final String accessToken, final Long linkedWalletId,
                                                         final LinkedMemberCreateRequest linkedMemberCreateRequest) {
        return sendPostRequest("/api/linked-wallets/%s/members".formatted(linkedWalletId),
                accessToken, linkedMemberCreateRequest);
    }

    private ExtractableResponse<Response> 링크지갑_참여자_조회_요청(final String accessToken, final Long linkedWalletId) {
        return sendGetRequest("/api/linked-wallets/%s/members".formatted(linkedWalletId), accessToken);
    }

    private ExtractableResponse<Response> 링크지갑_참여자_삭제_요청(final String accessToken, final Long linkedWalletId,
                                                         final Long linkedMemberId) {
        return sendDeleteRequest("/api/linked-wallets/%s/members?linkedMemberIds=%s"
                .formatted(linkedWalletId, linkedMemberId), accessToken);
    }
}