package monorail.linkpay.auth;

import monorail.linkpay.auth.dto.AuthToken;
import monorail.linkpay.auth.service.AuthTokenGenerator;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.token.TokenType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static monorail.linkpay.token.TokenFixtures.tokenGenerator;

class AuthTokenGeneratorTest {

    @Test
    public void 회원_정보로_인증_토큰을_생성한다() {
        //given
        Member member = Member.builder()
                .id(1L)
                .build();
        AuthTokenGenerator sut = new AuthTokenGenerator(tokenGenerator);

        //when
        AuthToken authToken = sut.generateFor(member, TokenType.ACCESS);

        //then
        Assertions.assertThat(authToken).isNotNull();
        Assertions.assertThat(authToken.value()).isNotBlank();
    }
}