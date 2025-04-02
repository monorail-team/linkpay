package monorail.linkpay.store.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.repository.StoreRepository;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreSignerManager storeSignerManager;
    private final IdGenerator idGenerator;

    @Transactional
    public Long create(final String name) {
        Store store = storeRepository.save(Store.builder()
                .id(idGenerator.generate())
                .name(name)
                .build());

        // 기본 가게 서명 1개 생성
        storeSignerManager.create(store);
        return store.getId();
    }
}
