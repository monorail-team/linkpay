package monorail.linkpay.auth.service;

import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.store.StoreFixtures;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.store.service.TransactionSignatureProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionSignatureKeyProviderTest {

    private TransactionSignatureProvider sut = new TransactionSignatureProvider();

    @Test
    public void 전자서명을_생성한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionResponse.Data.from(store, amount);
        var signatureKey = StoreFixtures.transactionSignatureKey(store);

        //when
        String signature = sut.createSignature(data, signatureKey.getEncryptKey());

        //then
        Assertions.assertThat(signature).isNotBlank();
    }

    @Test
    public void 전자서명을_검증한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionResponse.Data.from(store, amount);
        var signatureKey = StoreFixtures.transactionSignatureKey(store);
        String signature = sut.createSignature(data, signatureKey.getEncryptKey());

        //when, then
        Assertions.assertThatNoException().isThrownBy(() -> sut.verifySignature(data, signature, signatureKey.getDecryptKey()));
    }

    @Test
    public void 변조된_데이터로_전자서명_검증_시_예외가_발생한다() throws Exception{
        //given
        Store store = StoreFixtures.store();
        long amount = 1000L;
        var data = TransactionResponse.Data.from(store, amount);
        var signatureKey = StoreFixtures.transactionSignatureKey(store);
        String signature = sut.createSignature(data, signatureKey.getEncryptKey());
        var wrongData = TransactionResponse.Data.from(store, 222L);

        //when, then
        Assertions.assertThatThrownBy(() -> sut.verifySignature(wrongData, signature, signatureKey.getDecryptKey()))
                .isInstanceOf(LinkPayException.class)
                .extracting(e -> ((LinkPayException) e).getExceptionCode())
                .isEqualTo(ExceptionCode.FORBIDDEN_ACCESS);
    }
}