package monorail.linkpay.wallet.repository;

import java.util.Optional;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query(value = "select w from Wallet w where w.member.id = :memberId")
    Optional<Wallet> findByMemberId(@Param("memberId") Long memberId);
}
