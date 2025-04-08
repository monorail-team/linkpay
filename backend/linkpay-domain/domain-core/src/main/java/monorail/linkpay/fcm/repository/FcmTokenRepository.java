package monorail.linkpay.fcm.repository;

import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMemberId(Long memberId);

    List<FcmToken> findAllByMemberId(Long memberId);
}
