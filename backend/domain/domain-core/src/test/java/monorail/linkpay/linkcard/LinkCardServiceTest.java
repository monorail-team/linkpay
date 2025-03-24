package monorail.linkpay.linkcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import monorail.linkpay.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkCardServiceTest extends IntegrationTest {

    @Autowired
    private LinkCardService linkCardService;

    @Autowired
    private LinkCardRepository linkCardRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WalletRepository walletRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        linkCardRepository.deleteAllInBatch();
        walletRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        member = memberRepository.save(createMember());
        walletService.create(member);
    }

    @Test
    void 내_지갑에서_카드를_생성한다() {
        // given
        CreateLinkCardServiceRequest request = createCard(LocalDate.now().plusDays(1));

        // when
        linkCardService.create(member.getId(), request);

        LinkCard result = linkCardRepository.findByMember(member).orElseThrow();
        assertThat(result).isNotNull();
    }

    @Test
    void 카드만료일을_현재일_이전으로_설정시_오류가_발생한다(){
        // given
        CreateLinkCardServiceRequest request = createCard(LocalDate.now().minusDays(1));

        // when // then
        assertThatThrownBy(() -> linkCardService.create(member.getId(), request))
                .isInstanceOf(LinkPayException.class)
                .hasMessage("만료일은 현재일 이전으로 설정할 수 없습니다.");

    }

    private static CreateLinkCardServiceRequest createCard(LocalDate date) {
        return CreateLinkCardServiceRequest.builder()
                .cardName("test card")
                .expiratedAt(date)
                .limitPrice(new Point(500000))
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(1L)
                .email("linkpay@gmail.com")
                .username("link1")
                .build();
    }
}
