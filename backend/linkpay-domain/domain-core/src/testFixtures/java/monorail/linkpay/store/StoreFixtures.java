package monorail.linkpay.store;

import java.security.KeyPair;
import java.util.concurrent.atomic.AtomicLong;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSigner;
import monorail.linkpay.util.key.KeyAlgorithm;
import monorail.linkpay.util.key.KeyPairUtil;

public class StoreFixtures {
    private static final AtomicLong idGenerator = new AtomicLong();

    public static Store store() {
        return Store.builder()
                .id(idGenerator.getAndIncrement())
                .name("store1")
                .build();
    }

    public static StoreSigner storeSigner(Store store) {
        KeyPair keyPair = KeyPairUtil.generateKeyPair(KeyAlgorithm.RSA);
        return StoreSigner.builder()
                .id(idGenerator.getAndIncrement())
                .encryptKey(keyPair.getPrivate().getEncoded())
                .decryptKey(keyPair.getPublic().getEncoded())
                .store(store)
                .build();
    }

}
