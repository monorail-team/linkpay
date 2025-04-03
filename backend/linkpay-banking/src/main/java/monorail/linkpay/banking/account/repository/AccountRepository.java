package monorail.linkpay.banking.account.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import monorail.linkpay.banking.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :walletId")
    Optional<Account> findByWalletIdForUpdate(@Param("walletId") Long walletId);
}
