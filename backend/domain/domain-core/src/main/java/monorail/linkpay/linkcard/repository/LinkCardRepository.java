package monorail.linkpay.linkcard.repository;

import monorail.linkpay.linkcard.domain.CardState;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkCardRepository extends JpaRepository<LinkCard, Long> {

    Optional<LinkCard> findByMember(Member member);

    @Query("SELECT l FROM LinkCard l " +
            "WHERE l.wallet = :wallet " +
            "AND (:lastId IS NULL OR l.id < :lastId)" +
            "ORDER BY l.createdAt DESC ")
    Slice<LinkCard> findByWalletWithLastId(@Param("wallet") Wallet wallet, @Param("lastId") Long lastId, Pageable pageable);

    @Query("SELECT l FROM LinkCard l " +
            "WHERE l.wallet = :wallet " +
            "AND l.state = 'UNREGISTERED' " +
            "AND (:lastId IS NULL OR l.id < :lastId)" +
            "ORDER BY l.createdAt DESC ")
    Slice<LinkCard> findUnregisterByWalletWithLastId(@Param("wallet") Wallet wallet, @Param("lastId") Long lastId, Pageable pageable);
}
