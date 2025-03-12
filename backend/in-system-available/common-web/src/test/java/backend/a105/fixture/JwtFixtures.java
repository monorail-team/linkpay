package backend.a105.fixture;

import backend.a105.jwt.JwtProps;
import backend.a105.jwt.JwtProvider;
import backend.a105.token.TokenGenerator;
import backend.a105.util.id.IdGenerator;
import backend.a105.util.id.UniqueIdGenerator;

public class JwtFixtures {
    public static final JwtProps jwtProps = JwtProps.builder()
            .issuer("test")
            .secret("secret")
            .expirySeconds(3600)
            .refreshExpirySeconds(3600)
            .build();
    public static final JwtProvider jwtProvider = new JwtProvider(jwtProps);
    public static final IdGenerator idGenerator = new UniqueIdGenerator();

    public static JwtProvider jwtProvider(JwtProps props) {
        return new JwtProvider(props);
    }
}
