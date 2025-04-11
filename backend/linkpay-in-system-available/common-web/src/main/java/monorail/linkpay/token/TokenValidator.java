package monorail.linkpay.token;

import static monorail.linkpay.jwt.DefaultJwtClaim.PAYLOAD;
import static monorail.linkpay.jwt.DefaultJwtClaim.TOKEN_ID;
import static monorail.linkpay.jwt.DefaultJwtClaim.TOKEN_TYPE;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.jwt.JwtProvider;
import monorail.linkpay.jwt.JwtValidationException;
import monorail.linkpay.token.dto.ValidatedToken;
import monorail.linkpay.util.json.Json;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final JwtProvider jwtProvider;

    public ValidatedToken validate(final String token) throws TokenValidationException {
        DecodedJWT jwt = getDecodedJWT(token);
        Map<String, Claim> claims = jwt.getClaims();
        return ValidatedToken.builder()
                .type(TokenType.valueOf(claims.get(TOKEN_TYPE.name()).asString()))
                .tokenId(claims.get(TOKEN_ID.name()).asString())
                .expiresAt(jwt.getExpiresAtAsInstant())
                .payload(Json.of(claims.get(PAYLOAD.name()).asString())) // todo token 리팩토링
                .build();
    }

    private DecodedJWT getDecodedJWT(final String jwt) throws TokenValidationException {
        try {
            return jwtProvider.decode(jwt);
        } catch (JwtValidationException e) {
            throw new TokenValidationException();
        }
    }
}
