package backend.a105.token;

import backend.a105.jwt.JwtProps;
import backend.a105.jwt.JwtProvider;
import backend.a105.token.dto.GeneratedToken;
import backend.a105.type.Json;
import backend.a105.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtProvider jwtProvider;
    private final JwtProps jwtProps;
    private final IdGenerator idGenerator;

    public GeneratedToken generate(TokenType type, Json json) {
        long tokenId = idGenerator.generate();
        long expirySeconds = fetchExpirySeconds(type);
        String jwt = jwtProvider.generate(tokenId, type.toString(), expirySeconds, json);

        return GeneratedToken.builder()
                .tokenId(String.valueOf(tokenId))
                .type(type)
                .token(jwt)
                .build();
    }

    /**
     * @설명 토큰 타입에 따라 지정된 만료 시간을 조회한다.
     * @주의
     * @생각[주빈] 토큰에 대한 만료 시간을 토큰을 발급하는 JwtProvider 내부에서 결정하는 게 맞는지 고민중입니다.
     * 외부에서 결정하면?
     * 유지보수나 테스트하기 쉬워진다. 역할과 책임이 분명해진다.
     * 만료 시간은 보안과 관련된 정보인만큼 환경변수로 주입받는 게 낫지 않을까?
     * -> 외부에서 설정 값을 주입받으면 설정 정보가 @value를 통해 너무 분산되지 않을까?
     * 현재는 보안과 관련된 만큼 한 곳에서 엄격하게 관리하는 게 좋겠다는 생각에 Jwt를 직접 생성하는 Provider 내부에 위치시켰습니다.
     */
    private long fetchExpirySeconds(TokenType type) {
        assert !isNull(type);
        return switch (type) {
            case ACCESS -> jwtProps.getExpirySeconds();
            case REFRESH -> jwtProps.getRefreshExpirySeconds();
        };
    }
}
