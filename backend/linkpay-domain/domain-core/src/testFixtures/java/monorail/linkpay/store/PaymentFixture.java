package monorail.linkpay.store;

import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.service.TransactionSignatureProvider;

public class PaymentFixture {
    private static final TransactionSignatureProvider signatureProvider = new TransactionSignatureProvider();
//    private static final PaymentTokenProvider paymentTokenProvider = new PaymentTokenProvider();

    public static TransactionInfo.Data txData(Store store, Point point) {
        return TransactionInfo.Data.builder()
                .storeId(store.getId())
                .point(point)
                .build();
    }

    public static TransactionInfo txInfo(Store store, StoreSignature storeSignature, Point point) {
        var txData = txData(store, point);
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
