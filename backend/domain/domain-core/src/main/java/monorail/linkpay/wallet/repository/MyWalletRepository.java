package monorail.linkpay.wallet.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyWalletRepository extends JpaRepository<MyWallet, Long> {

    Optional<Wallet> findByMemberId(@Param("memberId") Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select mw from MyWallet mw where mw.member.id = :memberId")
    Optional<Wallet> findByMemberIdForUpdate(@Param("memberId") Long memberId);
}
