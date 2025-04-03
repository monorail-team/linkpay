package monorail.linkpay.wallet.service;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.wallet.domain.Wallet;

@SupportLayer
@RequiredArgsConstructor
public class WalletUpdater {

    private final WalletHistoryRecorder walletHistoryRecorder;

    public void chargePoint(final Wallet wallet, final Point amount, final Member member) {
        wallet.chargePoint(amount);
        walletHistoryRecorder.recordHistory(DEPOSIT, wallet, amount, member, null);
    }

    public void deductPoint(final Wallet wallet, final Point amount, final Member member, final Payment payment) {
        wallet.deductPoint(amount);
        walletHistoryRecorder.recordHistory(WITHDRAWAL, wallet, amount, member, payment);
    }
}
