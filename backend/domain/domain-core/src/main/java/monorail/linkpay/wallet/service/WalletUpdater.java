package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;

@SupportLayer
@RequiredArgsConstructor
public class WalletUpdater {
    private final WalletHistoryRecorder walletHistoryRecorder;

    public void chargePoint( final Wallet wallet, final Point amount, final Member member) {
        wallet.chargePoint(amount);
        walletHistoryRecorder.recordHistory(DEPOSIT, wallet, amount, member);
    }

    public void deductPoint( final Wallet wallet, final Point amount, final Member member) {
        wallet.deductPoint(amount);
        walletHistoryRecorder.recordHistory(WITHDRAWAL, wallet, amount, member);
    }
}
