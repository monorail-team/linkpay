package monorail.linkpay.fcm.repository;

import java.util.List;
import java.util.Optional;
import monorail.linkpay.fcm.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByMemberId(Long memberId);

    List<FcmToken> findAllByMemberId(Long memberId);

    Optional<FcmToken> findByTokenOrDeviceId(String token, String deviceId);
}
