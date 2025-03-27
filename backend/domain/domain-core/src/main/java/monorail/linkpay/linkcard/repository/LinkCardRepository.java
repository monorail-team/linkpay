package monorail.linkpay.linkcard.repository;

import java.time.LocalDateTime;
import java.util.List;
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

    List<LinkCard> findLinkCardsByMember(Member member);

    @Query("SELECT l FROM LinkCard l " +
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

    @Modifying
    @Query("UPDATE LinkCard l "
            + "SET l.state = 'REGISTERED' "
            + "WHERE l.id in :linkCardIds")
    void updateStateByIds(@Param("linkCardIds") Set<Long> linkCardIds);
}
