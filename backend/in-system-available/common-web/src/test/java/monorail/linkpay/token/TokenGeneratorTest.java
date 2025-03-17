package monorail.linkpay.token;

import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.util.json.Json;
import org.junit.jupiter.api.Test;

import static monorail.linkpay.jwt.JwtFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class TokenGeneratorTest {

    @Test
    public void Json_기반으로_토큰을_생성할_수_있다() throws Exception{
        //given
        TokenGenerator sut = new TokenGenerator(jwtProvider, jwtProps, idGenerator);
        String payload = """
                {"key":"value"}
                """;

        //when
        GeneratedToken token = sut.generate(TokenType.ACCESS, Json.of(payload));

        //then
        assertThat(token).isNotNull();
        assertThat(token.type()).isEqualTo(TokenType.ACCESS);
    }
}