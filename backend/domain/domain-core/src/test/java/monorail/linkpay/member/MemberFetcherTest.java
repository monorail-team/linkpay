package monorail.linkpay.member;

import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
class MemberFetcherTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    class 이메일로_회원_조회 {

        @Test
        public void 조회_성공() {
            //given
            String email = "email@email.com";
            memberRepository.save(Member.builder()
                    .id(1L)
                    .email(email)
                    .build());
            MemberFetcher sut = new MemberFetcher(memberRepository);

            //when
            Member member = sut.fetchByEmail(email);

            //then
            assertThat(member.getEmail()).isEqualTo(email);
        }

        @Test
        public void 조회_실패() {
            //given
            MemberFetcher sut = new MemberFetcher(memberRepository);

            //when, then
            Assertions.assertThatThrownBy(()->sut.fetchByEmail("wrong@email.com"))
                    .isInstanceOf(LinkPayException.class)
                    .extracting(e -> ((LinkPayException) e).getExceptionCode())
                    .isEqualTo(ExceptionCode.NOT_FOUND_RESOURCE);
        }
    }
}