package backend.a105.fixture;

import backend.a105.token.TokenGenerator;
import backend.a105.token.TokenValidator;

import static backend.a105.fixture.JwtFixtures.*;

public class TokenFixtures {
    public static final TokenGenerator tokenGenerator = new TokenGenerator(jwtProvider, jwtProps, idGenerator);
    public static final TokenValidator tokenValidator = new TokenValidator(jwtProvider);
}
