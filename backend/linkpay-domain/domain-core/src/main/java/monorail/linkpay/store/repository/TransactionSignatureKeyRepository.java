package monorail.linkpay.store.repository;

import monorail.linkpay.store.domain.TransactionSignatureKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionSignatureKeyRepository extends JpaRepository<TransactionSignatureKey, Long> {
    Optional<TransactionSignatureKey> findByStoreId(Long storeId);
}
