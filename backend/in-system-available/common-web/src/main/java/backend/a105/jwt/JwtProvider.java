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
import static java.util.Objects.isNull;

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


    /**
     * @param tokenId   토큰 식별자
     * @param tokenType 토큰 타입(사용처)
     * @param payload   JWT에 담을 페이로드
     * @return 서명된 JWT 문자열
     * @설명 서명된 JWT를 생성하는 메서드
     * @주의 tokenId는 토큰 식별에 사용되므로 unique값이 입력되도록 주의해주세요.
     * @생각[주빈] 로그인 뿐만 아니라, 결제용 토큰 등 다른 분야에도 JWT가 사용될 것으로 예상되어 공통 모듈로 사용할 수 있도록 구성했습니다.
     * 토큰 ID(만료 식별용/추적용)와 타입(용도 식별용)은 항상 공통적으로 필요하다고 생각해서 메서드 시그니처에 포함시켰습니다.
     * 나머지 정보는 Map의 형태로 호출하는 쪽에서 자유롭게 구성할 수 있는 게 더 낫다고 생각했습니다.
     * 범용성은 높으나 호출하는 쪽에서 편하게 쓰기 위해서는 별도로 어댑터 역할을 해줄 클래스를 추가로 구현해야 될 것 같습니다.
     */
    public String generate(long tokenId, TokenType tokenType, Map<String, Object> payload) {
        Instant expiresAt = Instant.now().plus(fetchExpirySeconds(tokenType), ChronoUnit.SECONDS);
        return JWT.create().withIssuer(props.issuer)
                .withPayload(payload)
                .withClaim(TOKEN_ID.toString(), tokenId)
                .withClaim(TOKEN_TYPE.toString(), tokenType.toString())
                .withClaim(SALT.toString(), randomSalt.nextInt())
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
            case ACCESS -> props.expirySeconds;
            case REFRESH -> props.refreshExpirySeconds;
        };
    }
}
