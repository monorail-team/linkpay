package monorail.linkpay.store.repository;

import monorail.linkpay.store.domain.StoreSigner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreSignerRepository extends JpaRepository<StoreSigner, Long> {
    Optional<StoreSigner> findByStoreId(Long storeId);
}
