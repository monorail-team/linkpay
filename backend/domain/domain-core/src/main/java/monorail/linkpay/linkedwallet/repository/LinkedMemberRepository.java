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

    @Query("select linkedWallet.id as linkedWalletId, "
            + "linkedWallet.name as linkedWalletName, "
            + "linkedWallet.point.amount as amount "
            + "from LinkedMember linkedMember "
            + "left join linkedMember.linkedWallet linkedWallet "
            + "where linkedMember.member.id = :memberId "
            + "and (:lastId is null or linkedWallet.id < :lastId) "
            + "order by linkedWallet.id desc")
    Slice<LinkedWalletDto> findByMemberId(@Param("memberId") Long memberId,
                                          @Param("lastId") Long lastId,
                                          Pageable pageable);

    Optional<LinkedMember> findByLinkedWalletIdAndMemberId(Long linkedWalletId, Long memberId);

    @Modifying
    @Query("update LinkedMember m "
            + "set m.status = 'DELETED', m.deletedAt = now() "
            + "where m.id in :linkedMemberIds")
    void deleteByIds(@Param("linkedMemberIds") Set<Long> linkedMemberIds,
                     @Param("deletedAt") LocalDateTime deletedAt);

    Optional<LinkedMember> findOneByMemberId(Long memberId);
}
