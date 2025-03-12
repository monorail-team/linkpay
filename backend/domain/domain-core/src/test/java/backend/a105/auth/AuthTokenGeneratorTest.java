package backend.a105.auth;

import backend.a105.auth.dto.AuthToken;
import backend.a105.auth.service.AuthTokenGenerator;
import backend.a105.member.domain.Member;
import backend.a105.token.TokenFixtures;
import backend.a105.token.TokenType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static backend.a105.token.TokenFixtures.tokenGenerator;

class AuthTokenGeneratorTest {

    @Test
    public void 회원_정보로_인증_토큰을_생성한다() throws Exception{
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