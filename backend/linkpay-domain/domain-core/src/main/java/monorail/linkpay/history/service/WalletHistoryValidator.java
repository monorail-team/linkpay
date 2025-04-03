package monorail.linkpay.history.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import monorail.linkpay.wallet.service.LinkedMemberValidator;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

@SupportLayer
@RequiredArgsConstructor
public class WalletHistoryValidator {

    private final MyWalletRepository myWalletRepository;
    private final LinkedMemberValidator linkedMemberValidator;

    public void validateRead(WalletHistory walletHistory, Member member) {
        checkOwnershipOrParticipant(walletHistory.getWallet(), member);
    }

    private void checkOwnershipOrParticipant(final Wallet wallet, final Member member) {
        myWalletRepository.findById(wallet.getId())
                .ifPresentOrElse(myWallet -> {
                    if (!myWallet.isMyWallet(member)) {
                        throw new LinkPayException(INVALID_REQUEST, "소유한 지갑이 아닙니다.");
                    }
                }, emptyAction(wallet, member));
    }

    private Runnable emptyAction(Wallet wallet, Member member) {
        return () -> linkedMemberValidator.validateIsLinkedMember(wallet.getId(), member.getId(), new LinkPayException(INVALID_REQUEST, "참여중인 링크지갑이 아닙니다."));
    }
}

