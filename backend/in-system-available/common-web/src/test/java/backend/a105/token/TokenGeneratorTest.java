package backend.a105.token;

import backend.a105.fixture.JwtFixtures;
import backend.a105.token.dto.GeneratedToken;
import backend.a105.util.json.Json;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static backend.a105.fixture.JwtFixtures.*;

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
        Assertions.assertThat(token).isNotNull();
    }
}