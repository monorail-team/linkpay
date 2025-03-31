package monorail.linkpay.banking.account.repository;

import monorail.linkpay.banking.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByWalletId(Long walletId);
}
