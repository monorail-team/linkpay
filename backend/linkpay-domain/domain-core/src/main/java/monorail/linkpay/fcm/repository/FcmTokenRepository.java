package monorail.linkpay.fcm.repository;

import jakarta.persistence.LockModeType;
import monorail.linkpay.fcm.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMemberId(Long memberId);

    List<FcmToken> findAllByMemberId(Long memberId);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<FcmToken> findByTokenOrDeviceId(String token, String deviceId);
}
