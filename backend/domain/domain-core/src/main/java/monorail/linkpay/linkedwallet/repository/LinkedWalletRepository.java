package monorail.linkpay.linkedwallet.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedWalletRepository extends JpaRepository<LinkedWallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select lw from LinkedWallet lw where lw.id = :id")
    Optional<LinkedWallet> findByIdForUpdate(@Param("id") Long id);
}
