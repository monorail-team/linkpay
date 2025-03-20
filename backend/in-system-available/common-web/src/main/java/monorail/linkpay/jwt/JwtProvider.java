package monorail.linkpay.jwt;

import monorail.linkpay.util.json.Json;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static monorail.linkpay.jwt.DefaultJwtClaim.*;

@Component
public class JwtProvider {

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;
    private final Random randomSalt;
    private final JwtProps props;

    public JwtProvider(JwtProps props) {
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
    public String generate(long tokenId, String tokenType, long expirySeconds, Json payload) {
        Instant expiresAt = Instant.now().plus(expirySeconds, ChronoUnit.SECONDS);
        return JWT.create().withIssuer(props.issuer)
                .withClaim(TOKEN_ID.name(), tokenId)
                .withClaim(TOKEN_TYPE.name(), tokenType)
                .withClaim(PAYLOAD.name(), payload.value())
                .withClaim(SALT.name(), randomSalt.nextInt())
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
}
