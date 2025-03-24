package monorail.linkpay.linkedwallet.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedMemberRepository extends JpaRepository<LinkedMember, Long> {

    Optional<LinkedMember> findByMemberId(Long memberId);

    @Modifying
    @Query("update LinkedMember m "
            + "set m.status = 'DELETED', m.deletedAt = :deletedAt "
            + "where m.id in :linkedMemberIds")
    void deleteByIds(@Param("linkedMemberIds") Set<Long> linkedMemberIds,
                     @Param("deletedAt") LocalDateTime deletedAt);
}
