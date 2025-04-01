package monorail.linkpay.store;

import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.payment.service.PaymentTokenProvider;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.service.TransactionSignatureProvider;

public class PaymentFixture {
    private static final TransactionSignatureProvider signatureProvider = new TransactionSignatureProvider();
    private static final Store store = StoreFixtures.store();
//    private static final PaymentTokenProvider paymentTokenProvider = new PaymentTokenProvider();

    public static TransactionInfo.Data txData(Point point) {
        return TransactionInfo.Data.builder()
                .storeId(StoreFixtures.store().getId())
                .point(point)
                .build();
    }

    public static TransactionInfo txInfo(Point point) {
        var txData = txData(point);
        StoreSignature storeSignature = StoreFixtures.storeSignature(store);
        String txSignature = signatureProvider.createSignature(txData, storeSignature.getEncryptKey());
        return TransactionInfo.from(txData, txSignature);
    }

    public static PaymentInfo payInfo(Member member, LinkCard linkCard) {
        return PaymentInfo.builder()
                .memberId(member.getId())
                .linkCardId(linkCard.getId())
                .paymentToken("temtpadmfpsadfmpsdapfpsd")
                .build();
    }
}
