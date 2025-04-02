package monorail.linkpay.linkcard.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.WalletFetcher;
import monorail.linkpay.wallet.service.WalletUpdater;
import org.springframework.transaction.annotation.Transactional;

@SupportLayer
@RequiredArgsConstructor
public class LinkCardUpdater {

    private final WalletFetcher walletFetcher;
    private final WalletUpdater walletUpdater;

    @Transactional
    public void pay(final LinkCard linkCard, final Point point, final Payment payment) {
        linkCard.usePoint(point);
        Wallet wallet = walletFetcher.fetchByIdForUpdate(linkCard.getWallet().getId());
        walletUpdater.deductPoint(wallet, point, linkCard.getMember(), payment);
    }
}
