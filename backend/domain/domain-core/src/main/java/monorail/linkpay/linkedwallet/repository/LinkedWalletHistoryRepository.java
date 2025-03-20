package monorail.linkpay.linkedwallet.repository;

import monorail.linkpay.linkedwallet.domain.LinkedWalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedWalletHistoryRepository extends JpaRepository<LinkedWalletHistory, Long> {
}
