package monorail.linkpay.linkcard.repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkCardRepository extends JpaRepository<LinkCard, Long> {

    List<LinkCard> findByMemberId(Long memberId);

    @Query("SELECT l from LinkCard l "
            + "WHERE l.member.id in :memberIds "
            + "AND l.wallet.id = :walletId")
    List<LinkCard> findByMemberIdInAndWalletId(Set<Long> memberIds, Long walletId);

    @Query("SELECT l from LinkCard l "
            + "WHERE l.member.id = :memberId "
            + "AND l.wallet.id = :walletId")
    List<LinkCard> findByMemberIdAndWalletId(Long memberId, Long walletId);

    @Query("SELECT EXISTS ("
            + "SELECT 1 FROM LinkCard l WHERE l.wallet.id = :walletId)")
    boolean existsByWalletId(Long walletId);

    @Query("SELECT l FROM LinkCard l " +
            "JOIN FETCH l.wallet " +
            "WHERE l.member.id = :memberId " +
            "AND l.state = :state " +
            "AND (:lastId IS NULL OR l.id < :lastId) " +
            "AND l.expiredAt > :current " +
            "ORDER BY l.id DESC ")
    Slice<LinkCard> findByStateWithLastId(@Param("memberId") Long memberId,
                                          @Param("lastId") Long lastId,
                                          @Param("state") CardState state,
                                          @Param("current") LocalDateTime current,
                                          Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LinkCard l "
            + "SET l.state = 'REGISTERED' "
            + "WHERE l.id in :linkCardIds")
    void updateStateByIds(@Param("linkCardIds") Set<Long> linkCardIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select l from LinkCard l where l.id = :linkCardId")
    Optional<LinkCard> findByIdForUpdate(@Param("linkCardId") Long linkCardId);

    @Query("""
                SELECT l FROM LinkCard l
                WHERE l.wallet.id = :walletId
            """)
    List<LinkCard> findAllByWalletId(Long walletId);
}
