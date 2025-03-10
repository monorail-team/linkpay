package backend.a105.jwt;

import backend.a105.util.IdGenerator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;

import static backend.a105.jwt.DefaultJwtClaim.*;

@Slf4j
@Component
public class JwtProvider {
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    private final Random randomSalt;
    private final JwtProps props;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Builder
    protected JwtProvider(JwtProps props, IdGenerator idGenerator) {
        this.props = props;
        this.algorithm = Algorithm.HMAC512(props.secret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(props.issuer)
                .build();
        this.randomSalt = new Random();
    }

    public String generate(long tokenId, JwtType type, Map<String, Object> payload) {
        Instant expiresAt = Instant.now().plus(fetchExpirySeconds(type), ChronoUnit.SECONDS);
        return JWT.create().withIssuer(props.issuer)
                .withPayload(payload)
                .withClaim(TOKEN_ID.value(), tokenId)
                .withClaim(TOKEN_TYPE.value(), type.name())
                .withClaim(SALT.value(), randomSalt.nextInt())
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public DecodedJWT decode(String jwt) throws JwtValidationException {
        try {
            return jwtVerifier.verify(jwt);

        } catch (JWTVerificationException e) {
            if (e instanceof TokenExpiredException) {
                throw new JwtValidationException("만료된 토큰입니다");
            } else {
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
}
