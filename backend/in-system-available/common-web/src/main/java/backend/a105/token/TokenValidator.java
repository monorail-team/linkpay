package backend.a105.token;

import backend.a105.jwt.JwtProvider;
import backend.a105.jwt.JwtValidationException;
import backend.a105.token.dto.ValidatedToken;
import backend.a105.util.json.Json;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static backend.a105.jwt.DefaultJwtClaim.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {
    private final JwtProvider jwtProvider;

    public ValidatedToken validate(String token) throws TokenValidationException {
        DecodedJWT jwt = getDecodedJWT(token);
        Map<String, Claim> claims = jwt.getClaims();
        return ValidatedToken.builder()
                .type(TokenType.valueOf(claims.get(TOKEN_TYPE.name()).asString()))
                .tokenId(claims.get(TOKEN_ID.name()).asString())
                .expiresAt(jwt.getExpiresAtAsInstant())
                .payload(Json.of(claims.get(PAYLOAD.name()).asString()))
                .build();
    }

    private DecodedJWT getDecodedJWT(String jwt) throws TokenValidationException {
        try {
            return jwtProvider.decode(jwt);
        } catch (JwtValidationException e) {
            throw new TokenValidationException();
        }
    }
}
