package monorail.linkpay.fcm;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.service.FcmService;
import monorail.linkpay.member.MemberFixtures;
import monorail.linkpay.member.domain.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class FcmServiceTest extends IntegrationTest {

    @Autowired
    FcmService sut;

    @Test
    public void 사용자_토큰을_저장한다() {
        //given
        Member member = memberRepository.save(MemberFixtures.member());
        String token = "fcm-token";
        String deviceId = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(3600, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.MILLIS);

        //when
        sut.register(member.getId(), token, deviceId, expiresAt);

        //then
        List<FcmToken> savedTokens = fcmTokenRepository.findAll();
        SoftAssertions.assertSoftly(softly -> {
            assertThat(savedTokens.size()).isEqualTo(1L);
            FcmToken out = savedTokens.getFirst();
            assertThat(out.getMember().getId()).isEqualTo(member.getId());
            assertThat(out.getToken()).isEqualTo(token);
            assertThat(out.getDeviceId()).isEqualTo(deviceId);
            assertThat(out.getExpiresAt()).isEqualTo(expiresAt);
        });
    }

    @Test
    void 동시_등록_테스트() throws InterruptedException {
        // given
        Long memberId = memberRepository.save(MemberFixtures.member()).getId();
        String token = "test-token";
        String deviceId = "test-device";
        Instant expiresAt = Instant.now().plus(3600, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.MILLIS);
        int exCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(exCount);

        // when
        for (int i = 0; i < exCount; i++) {
            executorService.submit(() -> {
                try {
                    sut.register(memberId, token, deviceId, expiresAt);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드 종료까지 대기

        // then
        List<FcmToken> all = fcmTokenRepository.findAll();
        assertThat(all).hasSize(1);
        FcmToken saved = all.get(0);
        assertThat(saved.getToken()).isEqualTo(token);
        assertThat(saved.getDeviceId()).isEqualTo(deviceId);
        assertThat(saved.getMember().getId()).isEqualTo(memberId);
    }
}