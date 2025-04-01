package monorail.linkpay.store.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.repository.StoreSignatureRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.util.key.KeyAlgorithm;
import monorail.linkpay.util.key.KeyPairUtil;
import org.springframework.transaction.annotation.Transactional;

@SupportLayer
@RequiredArgsConstructor
public class StoreSignatureManager {

    private final StoreSignatureRepository storeSignatureRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public void create(Store store) {
        KeyPair keyPair = KeyPairUtil.generateKeyPair(KeyAlgorithm.RSA);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        storeSignatureRepository.save(StoreSignature.builder()
                .id(idGenerator.generate())
                .store(store)
                .encryptKey(privateKey.getEncoded())
                .decryptKey(publicKey.getEncoded())
                .build());
    }
}
