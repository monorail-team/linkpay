package monorail.linkpay.history.repository;

import monorail.linkpay.history.domain.WalletHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

    @Query("SELECT wh FROM WalletHistory wh " +
            "WHERE wh.wallet.id = :walletId " +
            "AND (:lastId IS NULL OR wh.id < :lastId)" +
            "ORDER BY wh.id DESC ")
    Slice<WalletHistory> findByWalletIdWithLastId(@Param("walletId") Long walletId,
                                                  @Param("lastId") Long lastId,
                                                  Pageable pageable);
}
