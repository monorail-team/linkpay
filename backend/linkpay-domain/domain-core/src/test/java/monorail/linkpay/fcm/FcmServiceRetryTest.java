package monorail.linkpay.fcm;

import monorail.linkpay.MockTestConfiguration;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.fcm.repository.FcmTokenRepository;
import monorail.linkpay.fcm.service.FcmService;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(MockTestConfiguration.class)
@SpringBootTest(
        properties = {
                "banking.account.uri=http://localhost:8080/api/bank-account",
                "toss.secret-key=test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"
        }
)
@ActiveProfiles("test")
class FcmServiceRetryTest {

    @Autowired
    FcmService sut;

    @MockitoBean
    private FcmTokenRepository fcmTokenRepository;

    @MockitoBean
    private MemberFetcher memberFetcher;

    @Test
    void register_메서드는_예외_발생시_3회_재시도_수행한다() {
        // given
        Long memberId = 1L;
        String token = "token-retry";
        String deviceId = "device-retry";
        Instant expiresAt = Instant.now().plusSeconds(60);

        Member member = Member.builder().id(memberId).build();
        BDDMockito.given(memberFetcher.fetchById(memberId)).willReturn(member);

        // 내부 호출 시 항상 예외 발생하게 설정
        BDDMockito.willThrow(new OptimisticLockingFailureException("retry test"))
                .given(fcmTokenRepository).findByTokenOrDeviceId(token, deviceId);

        // when & then
        assertThatThrownBy(() ->
                sut.register(memberId, token, deviceId)
        ).isInstanceOf(LinkPayException.class)
                .hasMessageContaining("FCM 등록에 반복 실패");

        // retry 횟수 = 3회 (최대 시도)
        Mockito.verify(fcmTokenRepository, Mockito.times(3)).findByTokenOrDeviceId(token, deviceId);
    }
}