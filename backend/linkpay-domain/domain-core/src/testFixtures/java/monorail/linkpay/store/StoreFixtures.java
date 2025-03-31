package monorail.linkpay.store;

import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.util.key.KeyAlgorithm;
import monorail.linkpay.util.key.KeyPairUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

public class StoreFixtures {
    private static final AtomicLong idGenerator = new AtomicLong();

    public static Store store() {
        return Store.builder()
                .id(idGenerator.getAndIncrement())
                .name("store1")
                .build();
    }

    public static StoreSignature signature(Store store) {
        KeyPair keyPair = KeyPairUtil.generateKeyPair(KeyAlgorithm.RSA);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Base64.Encoder base64Encoder = Base64.getEncoder();

        return StoreSignature.builder()
                .id(idGenerator.getAndIncrement())
                .encryptKey(base64Encoder.encodeToString(privateKey.getEncoded()))
                .decryptKey(base64Encoder.encodeToString(publicKey.getEncoded()))
                .store(store)
                .build();
    }

}
