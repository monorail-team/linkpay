package monorail.linkpay.wallet.repository;

import monorail.linkpay.wallet.domain.WalletHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

    @Query("SELECT w FROM WalletHistory w " +
            "WHERE w.id = :walletId " +
            "AND (:lastId IS NULL OR w.id < :lastId) " +
            "ORDER BY w.createdAt DESC")
    Slice<WalletHistory> findByWalletIdWithLastId(
            @Param("walletId") Long walletId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );
}
