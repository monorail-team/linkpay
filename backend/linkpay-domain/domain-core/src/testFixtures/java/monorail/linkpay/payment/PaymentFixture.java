package monorail.linkpay.payment;

import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.payment.service.PaymentTokenProvider;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSigner;
import monorail.linkpay.token.TokenFixtures;
import monorail.linkpay.util.signature.SignatureProvider;

public class PaymentFixture {

    private static final SignatureProvider signatureProvider = new SignatureProvider();
    private static final PaymentTokenProvider paymentTokenProvider
            = new PaymentTokenProvider(TokenFixtures.tokenGenerator, TokenFixtures.tokenValidator);

    public static TransactionInfo.Data txData(Store store, Point point) {
        return TransactionInfo.Data.builder()
                .storeId(store.getId())
                .point(point)
                .build();
    }

    public static TransactionInfo txInfo(Store store, StoreSigner storeSigner, Point point) {
        var txData = txData(store, point);
        String txSignature = signatureProvider.sign(txData, storeSigner.getEncryptKey());
        return TransactionInfo.from(txData, txSignature);
    }

    public static PaymentInfo payInfo(Member member, LinkCard linkCard) {

        return PaymentInfo.builder()
                .memberId(member.getId())
                .linkCardId(linkCard.getId())
                .paymentToken(paymentTokenProvider.generateFor(member.getId()))
                .build();
    }
}
