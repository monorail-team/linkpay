package backend.a105.jwt;

import backend.a105.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtProvider jwtProvider;
    private final IdGenerator idGenerator;


    public GeneratedToken generate(JwtType type, Map<String, Object> payload) {
        long tokenId = idGenerator.generate();
        var jwtString = jwtProvider.generate(tokenId, type, payload);
        return GeneratedToken.builder()
                .tokenId(String.valueOf(tokenId))
                .type(type)
                .value(jwtString)
                .build();
    }
}
