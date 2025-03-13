package backend.a105.token;

import backend.a105.token.dto.GeneratedToken;
import backend.a105.token.dto.ValidatedToken;
import backend.a105.util.json.Json;
import org.junit.jupiter.api.Test;

import static backend.a105.jwt.JwtFixtures.jwtProvider;
import static backend.a105.token.TokenFixtures.tokenGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenValidatorTest {

    @Test
    public void 토큰_검증에_성공하면_Json을_획득한다() throws Exception{
        //given
        TokenValidator sut = new TokenValidator(jwtProvider);
        Json payload = Json.of("{\"test\":\"val\"}");
        GeneratedToken token = tokenGenerator.generate(TokenType.ACCESS, payload);

        //when
        ValidatedToken validatedToken = sut.validate(token.value());

        //then

        assertThat(validatedToken).isNotNull();
        assertThat(validatedToken.payload()).isEqualTo(payload);
    }

    @Test
    public void 토큰이_유효하지_않은_경우_예외가_발생한다() throws Exception{
        //given
        TokenValidator sut = new TokenValidator(jwtProvider);
        String token = "wrong";

        //when, then
        assertThatThrownBy(()->
        sut.validate(token))
                .isInstanceOf(IllegalArgumentException.class);
    }
}