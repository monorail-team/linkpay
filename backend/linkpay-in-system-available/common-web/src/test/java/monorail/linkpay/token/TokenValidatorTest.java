package monorail.linkpay.token;

import static monorail.linkpay.jwt.JwtFixtures.jwtProvider;
import static monorail.linkpay.token.TokenFixtures.tokenGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.token.dto.ValidatedToken;
import monorail.linkpay.util.json.Json;
import org.junit.jupiter.api.Test;

class TokenValidatorTest {

    @Test
    public void 토큰_검증에_성공하면_Json을_획득한다() throws Exception {
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
    public void 토큰이_유효하지_않은_경우_예외가_발생한다() throws Exception {
        //given
        TokenValidator sut = new TokenValidator(jwtProvider);
        String token = "wrong";

        //when, then
        assertThatThrownBy(() ->
                sut.validate(token))
                .isInstanceOf(TokenValidationException.class);
    }
}