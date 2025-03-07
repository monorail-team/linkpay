package backend.a105.jwt;

import backend.a105.util.IdGenerator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import static backend.a105.jwt.JwtClaim.*;

@Slf4j
@Component
public class JwtProvider {
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    private final Random randomSalt;
    private final IdGenerator idGenerator;
    private final JwtProps props;
    private final JwtParser jwtClaimParser;

    @Builder
    protected JwtProvider(JwtProps props, IdGenerator idGenerator, JwtParser jwtClaimParser) {
        this.props = props;
        this.algorithm = Algorithm.HMAC512(props.secret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(props.issuer)
                .build();
        this.randomSalt = new Random();
        this.idGenerator = idGenerator;
        this.jwtClaimParser = jwtClaimParser;
    }

    public SignedJwt generate(long memberId, List<String> roles, JwtType type) {
        long expirySeconds = fetchExpirySeconds(type);
        return SignedJwt.builder()
                .value(JWT.create().withIssuer(props.issuer)
                        .withClaim(TOKEN_ID.claim(), idGenerator.generate())
                        .withClaim(MEMBER_ID.claim(), memberId)
                        .withClaim(ROLES.claim(), roles)
                        .withClaim(TYPE.claim(), type.name())
                        .withClaim(SALT.claim(), randomSalt.nextInt())
                        .withExpiresAt(Instant.now().plus(expirySeconds, ChronoUnit.SECONDS))
                        .sign(algorithm))
                .expirySeconds(expirySeconds)
                .build();
    }

    public DecodedClaims validate(String jwt) throws JwtValidationException {
        return jwtClaimParser.parse(decode(jwt));
    }

    private DecodedJWT decode(String jwt) throws JwtValidationException {
        try {
            return jwtVerifier.verify(jwt);
        } catch (JWTVerificationException e) {
            if(e instanceof TokenExpiredException){
                throw new JwtValidationException("만료된 토큰입니다");
            }else {
                throw new JwtValidationException(e.getMessage());
            }
        }
    }

    private long fetchExpirySeconds(JwtType type) {
        return switch (type) {
            case ACCESS -> props.expirySeconds;
            case REFRESH -> props.refreshExpirySeconds;
        };
    }

    protected com.auth0.jwt.interfaces.JWTVerifier fetchVerifier() {
        return jwtVerifier;
    }
}
