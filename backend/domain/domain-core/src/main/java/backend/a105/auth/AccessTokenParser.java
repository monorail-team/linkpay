package backend.a105.auth;

import backend.a105.jwt.ValidatedToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenParser {

    public Object parse(ValidatedToken validatedToken) {
        Map<String, Object> payload = validatedToken.payload();
        // todo payload 파싱해서 필요한 VO로 파싱
        String memberId = (String) payload.get(AccessTokenClaim.MEMBER_ID);
        return null;
    }
}
