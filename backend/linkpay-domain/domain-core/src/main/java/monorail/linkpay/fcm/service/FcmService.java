package monorail.linkpay.fcm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.repository.FcmTokenRepository;
import monorail.linkpay.fcm.service.dto.FcmRegisterResponse;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final IdGenerator idGenerator;
    private final MemberFetcher memberFetcher;
    private final FcmSender fcmSender;

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2),
            retryFor = {
                    OptimisticLockingFailureException.class,
                    DataIntegrityViolationException.class
            },
            recover = "recoverRegisterFailure"
    )
    @Transactional
    public FcmRegisterResponse register(final Long memberId, final String token, final String deviceId) {
        Member member = memberFetcher.fetchById(memberId);
        Instant expiresAt = Instant.now().plusSeconds(60 * 60 * 24 * 30);
        Optional<FcmToken> fcmTokenOptional = fcmTokenRepository.findByTokenOrDeviceId(token, deviceId);

        if (fcmTokenOptional.isPresent()) {
            FcmToken existing = fcmTokenOptional.get();
            existing.update(member, token, deviceId, expiresAt);
            return new FcmRegisterResponse(expiresAt);
        }

        fcmTokenRepository.save(FcmToken.builder()
                .id(idGenerator.generate())
                .member(member)
                .token(token)
                .deviceId(deviceId)
                .expiresAt(expiresAt)
                .build());
        return new FcmRegisterResponse(expiresAt);
    }


    @Recover
    protected FcmRegisterResponse recoverRegisterFailure(Exception e, Long memberId, String token, String deviceId) {
        throw new LinkPayException(ExceptionCode.DUPLICATED_RESOURCE, "FCM 등록에 반복 실패하였습니다. 다시 시도해주세요.");
    }

    public void sendmessgae(final Long memberId, final String title, final String content) {
        fcmSender.send(memberId, title, content);
    }
}
