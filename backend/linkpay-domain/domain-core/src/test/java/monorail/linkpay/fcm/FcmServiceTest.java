package monorail.linkpay.fcm;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.fcm.domain.FcmToken;
import monorail.linkpay.fcm.service.FcmService;
import monorail.linkpay.member.domain.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FcmServiceTest extends IntegrationTest {

    @Autowired
    FcmService sut;

    @Test
    public void 사용자_토큰을_저장한다() {
        //given
        String token = "fcm-token";
        Member member = memberRepository.save(Member.builder()
                .id(1L)
                .username("link")
                .email("email@kakao.com").build());

        //when
        sut.register(member.getId(), token);

        //then
        List<FcmToken> savedTokens = fcmTokenRepository.findAll();
        SoftAssertions.assertSoftly(softly -> {
            assertThat(savedTokens.size()).isEqualTo(1);
            assertThat(savedTokens.getFirst().getMember().getId()).isEqualTo(1L);
            assertThat(savedTokens.getFirst().getToken()).isEqualTo(token);
        });
    }
}