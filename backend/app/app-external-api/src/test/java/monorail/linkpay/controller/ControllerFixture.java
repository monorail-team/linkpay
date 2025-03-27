package monorail.linkpay.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.controller.request.LinkCardRegistRequest;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.PaymentsRequest;
import monorail.linkpay.controller.request.SharedLinkCardCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.member.dto.MemberResponse;
import monorail.linkpay.wallet.dto.LinkedMemberResponse;
import monorail.linkpay.wallet.dto.LinkedMembersResponse;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.dto.WalletResponse;

public class ControllerFixture {

    public static final LoginResponse LOGIN_RESPONSE = new LoginResponse("accessToken");

    public static final MemberResponse MEMBER_RESPONSE = new MemberResponse(
            "1", "link1", "linked@gmail.com");
    public static final WalletResponse WALLET_RESPONSE = new WalletResponse(50000);

    public static final WalletPointRequest WALLET_POINT_REQUEST = new WalletPointRequest(50000);

    public static final LinkCardCreateRequest LINK_CARD_CREATE_REQUEST = new LinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.of(2025, 5, 25));

    public static final SharedLinkCardCreateRequest SHARED_LINK_CARD_CREATE_REQUEST = new SharedLinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.of(2025, 5, 25), List.of(1L), 1L);

    public static final LinkCardRegistRequest LINK_CARD_REGISTRATION_REQUEST = new LinkCardRegistRequest(List.of(1L));

    public static final LinkedMemberCreateRequest LINKED_MEMBER_CREATE_REQUEST = new LinkedMemberCreateRequest(1L);

    public static final LinkedWalletCreateRequest LINKED_WALLET_CREATE_REQUEST = new LinkedWalletCreateRequest(
            "링크지갑1", Set.of(1L, 2L, 3L));

    public static final LinkCardResponse LINK_CARD_RESPONSE_1 = new LinkCardResponse("1", 500000L,
            CardType.OWNED.name(),
            CardColor.getRandomColor().getHexCode(),
            "test card1",
            LocalDate.now().plusMonths(1),
            0L);

    public static final LinkCardResponse LINK_CARD_RESPONSE_2 = new LinkCardResponse("2", 500000L,
            CardType.OWNED.name(),
            CardColor.getRandomColor().getHexCode(),
            "test card2",
            LocalDate.now().plusMonths(1),
            0L);

    public static final LinkCardResponse SHARED_LINK_CARD_RESPONSE = new LinkCardResponse(
            "3",
            500000L,
            CardType.SHARED.name(),
            CardColor.getRandomColor().getHexCode(),
            "test card2",
            LocalDate.now().plusMonths(1),
            0L);

    public static final LinkCardsResponse LINK_CARDS_RESPONSE_1 = new LinkCardsResponse(
            List.of(LINK_CARD_RESPONSE_1, LINK_CARD_RESPONSE_2), false);
    public static final LinkCardsResponse LINK_CARDS_RESPONSE_2 = new LinkCardsResponse(
            List.of(LINK_CARD_RESPONSE_1, SHARED_LINK_CARD_RESPONSE), false);

    public static final LinkedWalletResponse LINKED_WALLET_RESPONSE_1 = new LinkedWalletResponse("1", "링크지갑1", 40000L,
            1);
    public static final LinkedWalletResponse LINKED_WALLET_RESPONSE_2 = new LinkedWalletResponse("2", "링크지갑2", 50000L,
            2);
    public static final LinkedWalletResponse LINKED_WALLET_RESPONSE_3 = new LinkedWalletResponse("3", "링크지갑3", 60000L,
            3);

    public static final LinkedWalletsResponse LINKED_WALLETS_RESPONSE = new LinkedWalletsResponse(
            List.of(LINKED_WALLET_RESPONSE_1, LINKED_WALLET_RESPONSE_2, LINKED_WALLET_RESPONSE_3),
            false
    );

    public static final PaymentsRequest PAYMENT_REQUEST = new PaymentsRequest(10000, 1L, 2L);

    public static final LinkedMemberResponse LINKED_MEMBER_RESPONSE_1 = new LinkedMemberResponse("1", "링크멤버1",
            "link1@gmail.com");
    public static final LinkedMemberResponse LINKED_MEMBER_RESPONSE_2 = new LinkedMemberResponse("2", "링크멤버2",
            "link2@gmail.com");
    public static final LinkedMemberResponse LINKED_MEMBER_RESPONSE_3 = new LinkedMemberResponse("3", "링크멤버3",
            "link3@gmail.com");

    public static final LinkedMembersResponse LINKED_MEMBERS_RESPONSE = new LinkedMembersResponse(
            List.of(LINKED_MEMBER_RESPONSE_1, LINKED_MEMBER_RESPONSE_2, LINKED_MEMBER_RESPONSE_3), false
    );
}
