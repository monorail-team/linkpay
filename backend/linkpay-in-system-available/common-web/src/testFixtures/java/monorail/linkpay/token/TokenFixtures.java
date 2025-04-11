package monorail.linkpay.token;

import static monorail.linkpay.jwt.JwtFixtures.idGenerator;
import static monorail.linkpay.jwt.JwtFixtures.jwtProps;
import static monorail.linkpay.jwt.JwtFixtures.jwtProvider;

public class TokenFixtures {

    public static final TokenGenerator tokenGenerator = new TokenGenerator(jwtProvider, jwtProps, idGenerator);
    public static final TokenValidator tokenValidator = new TokenValidator(jwtProvider);
}
