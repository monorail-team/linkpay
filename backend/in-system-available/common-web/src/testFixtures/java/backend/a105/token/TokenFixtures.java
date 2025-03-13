package backend.a105.token;

import static backend.a105.jwt.JwtFixtures.*;

public class TokenFixtures {
    public static final TokenGenerator tokenGenerator = new TokenGenerator(jwtProvider, jwtProps, idGenerator);
    public static final TokenValidator tokenValidator = new TokenValidator(jwtProvider);
}
