package monorail.linkpay.payment.repository;

import java.util.List;
import java.util.Set;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.dto.PaymentDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p " +
            "WHERE p.linkCard.id = :linkCardId " +
            "AND (:lastId IS NULL OR p.id < :lastId) " +
            "ORDER BY p.id DESC ")
    Slice<Payment> findPaymentsByLinkCard(@Param("linkCardId") Long linkCardId, @Param("lastId") Long lastId,
                                          PageRequest pageable);

    List<Payment> findAllByWalletHistoryIdIn(Set<Long> walletHistoryIds);

    @Query(value = """
    SELECT 
        p.payment_id AS paymentId,
        lc.link_card_id AS linkCardId,
        lc.card_name AS linkCardName,
        p.wallet_history_id AS walletHistoryId
    FROM payment p
    JOIN link_card lc ON p.link_card_id = lc.link_card_id
    WHERE p.wallet_history_id IN (:walletHistoryIds)
    """, nativeQuery = true)
    List<PaymentDto> findAllWithLinkCardByWalletHistoryIds(@Param("walletHistoryIds") Set<Long> walletHistoryIds);
}
