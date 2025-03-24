package monorail.linkpay.controller;

import java.time.LocalDate;
import java.util.Set;
import monorail.linkpay.auth.dto.LoginResponse;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.member.dto.MemberResponse;

public class ControllerFixture {

    public static final LoginResponse LOGIN_RESPONSE = new LoginResponse("accessToken");

    public static final WalletPointRequest CHARGE_REQUEST = new WalletPointRequest(50000);

    public static final LinkCardCreateRequest LINK_CARD_CREATE_REQUEST = new LinkCardCreateRequest(
            "테스트카드", 500000, LocalDate.of(2025, 5, 25));

    public static final MemberResponse MEMBER_RESPONSE = new MemberResponse(1L, "link1", "linked@gmail.com");

    public static final LinkedWalletCreateRequest LINKED_WALLET_CREATE_REQUEST = new LinkedWalletCreateRequest(
            "링크지갑1", Set.of(1L, 2L, 3L));

    public static final LinkedMemberCreateRequest LINKED_MEMBER_CREATE_REQUEST = new LinkedMemberCreateRequest(1L);
}
