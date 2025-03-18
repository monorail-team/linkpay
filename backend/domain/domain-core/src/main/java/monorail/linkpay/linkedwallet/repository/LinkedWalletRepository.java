package monorail.linkpay.linkedwallet.repository;

import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedWalletRepository extends JpaRepository<LinkedWallet, Long> {
}
