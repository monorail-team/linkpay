package monorail.linkpay.wallet.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedWalletRepository extends JpaRepository<LinkedWallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select lw from LinkedWallet lw where lw.id = :id")
    Optional<LinkedWallet> findByIdForUpdate(@Param("id") Long id);

    @Query("select lw.id as linkedWalletId, "
            + "lw.name as linkedWalletName, "
            + "lw.point.amount as amount, "
            + "(select count(lm2) from LinkedMember lm2 where lm2.linkedWallet.id = lw.id) as participantCount "
            + "from LinkedMember lm "
            + "join lm.linkedWallet lw "
            + "where lm.member.id = :memberId "
            + "and lm.role = :role "
            + "and (:lastId is null or lw.id < :lastId) "
            + "group by lw.id, lw.name, lw.point.amount "
            + "order by lw.id desc")
    Slice<LinkedWalletDto> findLinkedWalletsByMemberId(@Param("memberId") Long memberId,
                                                       @Param("role") Role role,
                                                       @Param("lastId") Long lastId,
                                                       Pageable pageable);
}
