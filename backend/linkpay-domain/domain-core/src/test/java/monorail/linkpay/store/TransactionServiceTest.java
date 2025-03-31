package monorail.linkpay.store;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.store.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionServiceTest extends IntegrationTest {

    @Autowired
    private TransactionService sut;

    @Test
    public void 거래정보를_생성한다() throws Exception{
        //given
        Store store = storeRepository.save(StoreFixtures.store());
        transactionSignatureKeyRepository.save(StoreFixtures.transactionSignatureKey(store));
        long storeId =store.getId();
        long amount = 1000L;

        //when
        TransactionResponse response = sut.create(storeId, amount);

        //then
        assertThat(response.data()).isNotNull();
        assertThat(response.data().amount()).isEqualTo(amount);
        assertThat(response.signature()).isNotNull();
    }
}