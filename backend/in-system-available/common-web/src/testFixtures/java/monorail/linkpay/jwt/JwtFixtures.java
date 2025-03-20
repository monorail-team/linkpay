package monorail.linkpay.jwt;

import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.util.id.UniqueIdGenerator;

public class JwtFixtures {

    public static final JwtProps jwtProps = JwtProps.builder()
            .issuer("test")
            .secret("secret")
            .expirySeconds(3600)
            .refreshExpirySeconds(3600)
            .build();
    public static final JwtProvider jwtProvider = new JwtProvider(jwtProps);
    public static final IdGenerator idGenerator = new UniqueIdGenerator();

    public static JwtProvider jwtProvider(final JwtProps props) {
        return new JwtProvider(props);
    }
}
