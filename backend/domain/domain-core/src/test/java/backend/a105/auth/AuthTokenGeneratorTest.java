package backend.a105.auth;

import backend.a105.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthTokenGeneratorTest {

    @Test
    public void 회원_정보로_인증_토큰을_생성한다() throws Exception{
        //given
        Member member = Member.builder()
                .build();
//        AuthTokenGenerator sut = new AuthTokenGenerator();

        //when
//        AuthToken authToken = sut.generateFor(member);

        //then
//        Assertions.assertThat(authToken).isNotNull();
//        Assertions.assertThat(authToken.value()).isNotBlank();
    }
}