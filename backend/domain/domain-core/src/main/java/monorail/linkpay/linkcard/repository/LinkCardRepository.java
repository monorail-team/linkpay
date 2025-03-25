package monorail.linkpay.linkcard.repository;

import java.util.Optional;
import java.util.Set;
import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkCardRepository extends JpaRepository<LinkCard, Long> {

    Optional<LinkCard> findByMember(Member member);

    @Query("SELECT l FROM LinkCard l " +
            "WHERE l.wallet.id = :walletId " +
            "AND (:lastId IS NULL OR l.id < :lastId)" +
            "ORDER BY l.id DESC ")
    Slice<LinkCard> findByWalletWithLastId(@Param("walletId") Long walletId,
                                           @Param("lastId") Long lastId,
                                           Pageable pageable);


    @Query("SELECT l FROM LinkCard l " +
            "WHERE l.wallet.id = :walletId " +
            "AND l.state = :state " +
            "AND (:lastId IS NULL OR l.id < :lastId)" +
            "ORDER BY l.id DESC ")
    Slice<LinkCard> findByStateAndWalletWithLastId(@Param("walletId") Long walletId,
                                                   @Param("lastId") Long lastId,
                                                   Pageable pageable,
                                                   @Param("state") CardState state);

    @Modifying
    @Query("UPDATE LinkCard l "
            + "SET l.state = 'REGISTERED' "
            + "WHERE l.id in :linkCardIds")
    void updateStateById(@Param("linkCardIds") Set<Long> linkCardIds);
}
