package monorail.linkpay.store;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.service.StoreTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class StoreTransactionServiceTest extends IntegrationTest {

    @Autowired
    private StoreTransactionService sut;

    @Test
    public void 거래정보를_생성한다() throws Exception{
        //given
        Store store = storeRepository.save(StoreFixtures.store());
        storeSignerRepository.save(StoreFixtures.storeSigner(store));
        long storeId =store.getId();
        Point price = new Point(1000L);

        //when
        TransactionInfo txInfo = sut.create(storeId, price);

        //then
        assertThat(txInfo.data()).isNotNull();
        assertThat(txInfo.data().point()).isEqualTo(price);
        assertThat(txInfo.signature()).isNotNull();
    }
}