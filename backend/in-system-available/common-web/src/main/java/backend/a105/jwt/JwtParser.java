package backend.a105.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static backend.a105.jwt.JwtClaim.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtParser {

    public DecodedClaims parse(DecodedJWT decodedJWT){
        Map<String, Claim> claims = decodedJWT.getClaims();
        var tokenId = claims.get(TOKEN_ID.claim()).asString();
        var userId = claims.get(MEMBER_ID.claim()).asString();
        var type = JwtType.valueOf(claims.get(TYPE.claim()).asString());
        var roles = claims.get(ROLES.claim()).asList(String.class);
        var expiresAt = decodedJWT.getExpiresAtAsInstant();
        return new DecodedClaims(tokenId, userId, type, roles, expiresAt);
    }
}
