package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSignature;
import monorail.linkpay.store.repository.StoreRepository;
import monorail.linkpay.store.repository.StoreSignatureRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.util.key.KeyAlgorithm;
import monorail.linkpay.util.key.KeyPairUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreSignatureRepository storeSignatureRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(final String name) {
        Store store = storeRepository.save(Store.builder()
                .id(idGenerator.generate())
                .name(name)
                .build());

        // 기본 가게 서명 1개 생성
        KeyPair keyPair = KeyPairUtil.generateKeyPair(KeyAlgorithm.RSA);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        storeSignatureRepository.save(StoreSignature.builder()
                .id(idGenerator.generate())
                .store(store)
                .encryptKey(privateKey.getEncoded())
                .decryptKey(publicKey.getEncoded())
                .build());

        return store.getId();
    }
}
