package backend.a105.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public ValidatedToken validate(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        return ValidatedToken.builder()
                .type(TokenType.valueOf(extract(TOKEN_TYPE, claims)))
                .tokenId(extract(TOKEN_ID, claims))
                .expiresAt(decodedJWT.getExpiresAtAsInstant())
                .payload(getPayload(decodedJWT))
                .build();
    }

    private static String extract(DefaultJwtClaim key, Map<String, Claim> claims) {
        return claims.get(key.toString()).asString();
    }

    private DecodedJWT getDecodedJWT(String jwt) {
        try {
            return jwtProvider.decode(jwt);
        } catch (JwtValidationException e) {
            throw new RuntimeException(e); // todo
        }
    }

    private Map<String, Object> getPayload(DecodedJWT decode) {
        try {
            return objectMapper.readValue(decode.getPayload(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
