package monorail.linkpay.linkedwallet.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.repository.dto.LinkedWalletDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedMemberRepository extends JpaRepository<LinkedMember, Long> {

    Optional<LinkedMember> findByLinkedWalletIdAndMemberId(Long linkedWalletId, Long memberId);

    @Query("select count(lm) as participantCount, "
            + "lw.id as linkedWalletId, "
            + "lw.name as linkedWalletName, "
            + "lw.point.amount as amount "
            + "from LinkedMember lm "
            + "join lm.linkedWallet lw "
            + "where lm.member.id = :memberId "
            + "and (:lastId is null or lw.id < :lastId) "
            + "group by lw.id, lw.name, lw.point.amount "
            + "order by lw.id desc")
    Slice<LinkedWalletDto> findByMemberId(@Param("memberId") Long memberId,
                                          @Param("lastId") Long lastId,
                                          Pageable pageable);

    int countByLinkedWalletId(@Param("linkedWalletId") Long linkedWalletId);

    @Modifying
    @Query("update LinkedMember m "
            + "set m.deletedAt = CURRENT_TIMESTAMP "
            + "where m.id in :linkedMemberIds")
    void deleteByIds(@Param("linkedMemberIds") Set<Long> linkedMemberIds,
                     @Param("deletedAt") LocalDateTime deletedAt);

    Optional<LinkedMember> findOneByMemberId(Long memberId);
}
