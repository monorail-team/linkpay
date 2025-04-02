package monorail.linkpay.store.repository;

import monorail.linkpay.store.domain.StoreSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreSignatureRepository extends JpaRepository<StoreSignature, Long> {
    Optional<StoreSignature> findByStoreId(Long storeId);
}
