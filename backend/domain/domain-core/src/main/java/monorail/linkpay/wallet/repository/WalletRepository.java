package monorail.linkpay.wallet.repository;

import jakarta.persistence.LockModeType;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByMemberId(@Param("memberId") Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select w from Wallet w where w.member.id = :memberId")
    Optional<Wallet> findByMemberIdForUpdate(@Param("memberId") Long memberId);
}
