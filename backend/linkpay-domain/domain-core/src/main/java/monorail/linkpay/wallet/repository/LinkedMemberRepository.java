package monorail.linkpay.wallet.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedMemberRepository extends JpaRepository<LinkedMember, Long> {

    boolean existsByLinkedWalletIdAndMemberId(Long linkedWalletId, Long memberId);

    int countByLinkedWalletId(Long linkedWalletId);

    Optional<LinkedMember> findByMemberId(Long memberId);

    Optional<LinkedMember> findByLinkedWalletIdAndMemberId(Long linkedWalletId, Long memberId);

    @EntityGraph(attributePaths = {"member"})
    List<LinkedMember> findByIdIn(Set<Long> linkedMemberIds);

    List<LinkedMember> findAllByLinkedWalletId(Long linkedWalletId);

    @Query("select lm from LinkedMember lm "
            + "join fetch lm.member "
            + "where lm.linkedWallet.id = :linkedWalletId "
            + "and (:lastId is null or lm.id < :lastId) "
            + "order by lm.id desc")
    Slice<LinkedMember> findAllByLinkedWalletId(@Param("linkedWalletId") Long linkedWalletId,
                                                @Param("lastId") Long lastId,
                                                Pageable pageable);

    @Modifying
    @Query("update LinkedMember m "
            + "set m.deletedAt = CURRENT_TIMESTAMP "
            + "where m.id in :linkedMemberIds")
    void deleteByIds(@Param("linkedMemberIds") Set<Long> linkedMemberIds);

    @Query("select lm from LinkedMember lm "
            + "where lm.linkedWallet.id = :linkedWalletId "
            + "and lm.role = :role")
    LinkedMember findByLinkedWalletIdAndRole(@Param("linkedWalletId") Long linkedWalletId,
                                             @Param("role") Role role);

    @Modifying
    @Query(value = """
            update linked_member 
            set deleted_at = null 
            where member_id = :memberId 
              and wallet_id = :linkedWalletId 
              and deleted_at is not null
            """, nativeQuery = true)
    int restoreDeletedLinkedMember(@Param("memberId") Long memberId,
                                   @Param("linkedWalletId") Long linkedWalletId);
}
