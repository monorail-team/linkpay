package monorail.linkpay.payment.repository;

import monorail.linkpay.payment.domain.Payment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p " +
            "WHERE p.linkCard.id = :linkCardId " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "ORDER BY p.id DESC ")
    Slice<Payment> findPaymentsByLinkCard(@Param("linkCardId") Long linkCardId, @Param("lastId") Long lastId,
                                          PageRequest pageable);

    List<Payment> findAllByWalletHistoryIdIn(Set<Long> walletHistoryIds);
}
