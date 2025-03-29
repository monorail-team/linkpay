package monorail.linkpay.wallet.repository;

import jakarta.persistence.LockModeType;
import monorail.linkpay.wallet.domain.MyWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyWalletRepository extends JpaRepository<MyWallet, Long> {

    Optional<MyWallet> findByMemberId(@Param("memberId") Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select mw from MyWallet mw where mw.member.id = :memberId")
    Optional<MyWallet> findByMemberIdForUpdate(@Param("memberId") Long memberId);
}
