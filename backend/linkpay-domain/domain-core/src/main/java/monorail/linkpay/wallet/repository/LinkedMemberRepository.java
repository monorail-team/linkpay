package monorail.linkpay.wallet.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import monorail.linkpay.wallet.domain.LinkedMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedMemberRepository extends JpaRepository<LinkedMember, Long> {

    int countByLinkedWalletId(Long linkedWalletId);

    Optional<LinkedMember> findByMemberId(Long memberId);

    Optional<LinkedMember> findByLinkedWalletIdAndMemberId(Long linkedWalletId, Long memberId);

    List<LinkedMember> findByLinkedWalletId(Long linkedWalletId);

    @Query("select lm from LinkedMember lm "
            + "join fetch lm.member "
            + "where lm.linkedWallet.id = :linkedWalletId "
            + "and (:lastId is null or lm.id < :lastId)")
    Slice<LinkedMember> findAllByLinkedWalletId(@Param("linkedWalletId") Long linkedWalletId,
                                                @Param("lastId") Long lastId,
                                                Pageable pageable);

    @Modifying
    @Query("update LinkedMember m "
            + "set m.deletedAt = CURRENT_TIMESTAMP "
            + "where m.id in :linkedMemberIds")
    void deleteByIds(@Param("linkedMemberIds") Set<Long> linkedMemberIds);
}
