package monorail.linkpay.wallet.repository;

import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByMember(Member member);

    @Modifying
    @Query(value = "update wallet w " +
                    "set w.amount = w.amount + :amount " +
                    "where w.member_id = :memberId",
        nativeQuery = true
    )
    void updateAmount(@Param("memberId") Long memberId, @Param("amount") Long amount);
}
