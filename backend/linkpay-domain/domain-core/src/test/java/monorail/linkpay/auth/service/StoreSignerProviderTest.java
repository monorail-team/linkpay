package monorail.linkpay.auth.service;

import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.StoreFixtures;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.util.signature.SignatureProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StoreSignerProviderTest {

    private SignatureProvider sut = new SignatureProvider();

    @Test
    public void 전자서명을_생성한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionInfo.Data.from(store, amount);
        var signature = StoreFixtures.storeSigner(store);

        //when
        String signedSignature = sut.sign(data, signature.getEncryptKey());

        //then
        Assertions.assertThat(signedSignature).isNotBlank();
    }

    @Test
    public void 전자서명을_검증한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionInfo.Data.from(store, amount);
        var signatureKey = StoreFixtures.storeSigner(store);
        String signature = sut.sign(data, signatureKey.getEncryptKey());

        //when, then
        Assertions.assertThatNoException().isThrownBy(() -> sut.verify(data, signature, signatureKey.getDecryptKey()));
    }

    @Test
    public void 변조된_데이터로_전자서명_검증_시_예외가_발생한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionInfo.Data.from(store, amount);
        var signatureKey = StoreFixtures.storeSigner(store);
        String signature = sut.sign(data, signatureKey.getEncryptKey());
        var wrongData = TransactionInfo.Data.from(store, 222L);

        //when, then
        Assertions.assertThatThrownBy(() -> sut.verify(wrongData, signature, signatureKey.getDecryptKey()))
                .isInstanceOf(LinkPayException.class)
                .extracting(e -> ((LinkPayException) e).getExceptionCode())
                .isEqualTo(ExceptionCode.FORBIDDEN_ACCESS);
    }
}