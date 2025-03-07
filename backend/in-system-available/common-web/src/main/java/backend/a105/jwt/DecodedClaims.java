package backend.a105.jwt;

import java.time.Instant;
import java.util.List;

public record DecodedClaims(String tokenId,
                            String userId,
                            JwtType type,
                            List<String> roles,
                            Instant expiresAt) {
}