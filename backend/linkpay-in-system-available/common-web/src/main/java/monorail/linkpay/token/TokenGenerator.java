package monorail.linkpay.token;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.jwt.JwtProps;
import monorail.linkpay.jwt.JwtProvider;
import monorail.linkpay.token.dto.GeneratedToken;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.util.json.Json;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtProvider jwtProvider;
    private final JwtProps jwtProps;
    private final IdGenerator idGenerator;

    /**
     * @설명 TokenType과 일치하는 타입의 토큰을 생성한다. json으로 입력된 값들을 페이로드로 갖는다
     * @주의 Json타입은 Plain String Json의 Wrapper Class임에 주의
     * @생각[주빈] 토큰을 만드는 사람은 단순히 어떤 타입의, 어떤 값들을 포함하는 토큰을 만드는지에만 관심이 있다고 생각했습니다. 따라서 범용성 있는 payload 필드를 사용해서 토큰에 포함될 값을
     * 지정했습니다. 세부적인 토큰과 관련된 정책(어떤 타입인지, 만료일 설정 등)을 캡슐화했습니다. 토큰을 사용하는 객체 입장에서는 내부적으로 jwt를 쓰는지 어떤 다른 토큰을 쓰는지 몰라도 된다고
     * 생각했습니다.
     */
    public GeneratedToken generate(final TokenType type, final Json payload) {
        assert !isNull(type);
        assert !isNull(payload);
        long tokenId = idGenerator.generate();
        long expirySeconds = fetchExpirySeconds(type);
        String jwt = jwtProvider.generate(tokenId, type.toString(), expirySeconds, payload);

        return GeneratedToken.builder()
                .tokenId(String.valueOf(tokenId))
                .type(type)
                .value(jwt)
                .build();
    }

    /**
     * @설명 토큰 타입에 따라 지정된 만료 시간을 조회한다.
     * @주의
     * @생각[주빈] 토큰에 대한 만료 시간을 토큰을 발급하는 JwtProvider 내부에서 결정하는 게 맞는지 고민중입니다. 외부에서 결정하면? 유지보수나 테스트하기 쉬워진다. 역할과 책임이 분명해진다. 만료
     * 시간은 보안과 관련된 정보인만큼 환경변수로 주입받는 게 낫지 않을까? -> 외부에서 설정 값을 주입받으면 설정 정보가 @value를 통해 너무 분산되지 않을까? 현재는 보안과 관련된 만큼 한 곳에서
     * 엄격하게 관리하는 게 좋겠다는 생각에 Jwt를 직접 생성하는 Provider 내부에 위치시켰습니다.
     */
    private long fetchExpirySeconds(final TokenType type) {
        return switch (type) {
            case ACCESS -> jwtProps.getExpirySeconds();
            case REFRESH -> jwtProps.getRefreshExpirySeconds();
            case PAYMENT -> 45;
        };
    }
}
