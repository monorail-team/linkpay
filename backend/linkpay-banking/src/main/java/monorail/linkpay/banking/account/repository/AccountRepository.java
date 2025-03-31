package monorail.linkpay.banking.account.repository;

import java.util.Optional;
import monorail.linkpay.banking.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByWalletId(Long walletId);
}
