package monorail.linkpay.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.controller.request.LinkCardRegistRequest;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.SharedLinkCardCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.member.dto.MemberResponse;
import monorail.linkpay.wallet.dto.WalletResponse;

public class ControllerFixture {

    public static final LoginResponse LOGIN_RESPONSE = new LoginResponse("accessToken");

    public static final WalletPointRequest CHARGE_REQUEST = new WalletPointRequest(50000);

    public static final LinkCardCreateRequest LINK_CARD_CREATE_REQUEST = new LinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.of(2025, 5, 25));

    public static final SharedLinkCardCreateRequest SHARED_LINK_CARD_CREATE_REQUEST = new SharedLinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.of(2025, 5, 25), List.of(1L), 1L);

    public static final LinkCardRegistRequest LINK_CARD_REGISTRATION_REQUEST = new LinkCardRegistRequest(List.of(1L));

    public static final MemberResponse MEMBER_RESPONSE = new MemberResponse(1L, "link1", "linked@gmail.com");

    public static final WalletResponse WALLET_RESPONSE = new WalletResponse(50000);

    public static final LinkedWalletCreateRequest LINKED_WALLET_CREATE_REQUEST = new LinkedWalletCreateRequest(
            "링크지갑1", Set.of(1L, 2L, 3L));

    public static final LinkedMemberCreateRequest LINKED_MEMBER_CREATE_REQUEST = new LinkedMemberCreateRequest(1L);

    public static final LinkCardResponse LINK_CARD_RESPONSE = new LinkCardResponse(1L, 500000L,
            CardType.OWNED.name(),
            CardColor.getRandomColor().getHexCode(),
            "test card",
            LocalDate.now().plusMonths(1),
            0L);

    public static final LinkCardResponse SHARED_LINK_CARD_RESPONSE = new LinkCardResponse(2L, 500000L,
            CardType.SHARED.name(),
            CardColor.getRandomColor().getHexCode(),
            "test card2",
            LocalDate.now().plusMonths(1),
            0L);

    public static final LinkCardsResponse LINK_CARDS_RESPONSE = getLinkCardsResponse(
            List.of(LINK_CARD_RESPONSE, SHARED_LINK_CARD_RESPONSE));

    public static LinkCardsResponse getLinkCardsResponse(List<LinkCardResponse> linkCardResponse) {
        return new LinkCardsResponse(linkCardResponse, false);
    }
}
