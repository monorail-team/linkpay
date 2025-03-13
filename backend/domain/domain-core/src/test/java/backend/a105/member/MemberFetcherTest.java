package backend.a105.member;

import backend.a105.exception.AppException;
import backend.a105.exception.ExceptionCode;
import backend.a105.member.domain.Member;
import backend.a105.member.repository.MemberRepository;
import backend.a105.member.service.MemberFetcher;
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
        public void 조회_성공() throws Exception {
            //given
            String email = "email@email.com";
            memberRepository.save(Member.builder()
                    .id(1L)
                    .email(email)
                    .build());
            MemberFetcher sut = new MemberFetcher(memberRepository);

            //when
            Member member = sut.fetchBy(email);

            //then
            assertThat(member.getEmail()).isEqualTo(email);
        }

        @Test
        public void 조회_실패() throws Exception {
            //given
            MemberFetcher sut = new MemberFetcher(memberRepository);

            //when, then
            Assertions.assertThatThrownBy(()->sut.fetchBy("wrong@email.com"))
                    .isInstanceOf(AppException.class)
                    .extracting(e -> ((AppException) e).getExceptionCode())
                    .isEqualTo(ExceptionCode.NOT_FOUND_RESOURCE);
        }
    }
}