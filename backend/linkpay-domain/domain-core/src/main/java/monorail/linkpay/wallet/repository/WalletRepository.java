package monorail.linkpay.wallet.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select w from Wallet w where w.id = :walletId")
    Optional<Wallet> findByIdForUpdate(@Param("walletId") Long walletId);

    @Query(value = "select w.wallet_type from wallet w where w.wallet_id = :walletId", nativeQuery = true)
    Optional<String> findTypeByWalletId(@Param("walletId") Long walletId);
}
