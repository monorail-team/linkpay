package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.LinkedMemberValidator;
import org.springframework.transaction.annotation.Transactional;

import static monorail.linkpay.exception.ExceptionCode.FORBIDDEN_ACCESS;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

@SupportLayer
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletHistoryValidator {

    private final LinkedMemberValidator linkedMemberValidator;

    public void validateRead(final WalletHistory walletHistory, final Member member) {
        checkOwnershipOrParticipant(walletHistory.getWallet(), member);
    }

    private void checkOwnershipOrParticipant(final Wallet wallet, final Member member) {
        switch (wallet) {
            case MyWallet myWallet -> {
                if (!myWallet.isMyWallet(member)) {
                    throw new LinkPayException(FORBIDDEN_ACCESS, "소유한 지갑이 아닙니다.");
                }
            }
            case LinkedWallet linkedWallet -> {
                linkedMemberValidator.validateIsLinkedMember(linkedWallet.getId(), member.getId(), new LinkPayException(FORBIDDEN_ACCESS, "참여중인 링크지갑이 아닙니다."));
            }
            default -> {
                throw new LinkPayException(INVALID_REQUEST, "처리할 수 없는 종류의 지갑입니다.");
            }
        }
    }
}

