package monorail.linkpay.auth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record KakaoOauthResponse(
        @JsonProperty("access_token") String accessToken
) {
    public static KakaoOauthResponse of(String accessToken) {
        return new KakaoOauthResponse(accessToken);
    }
}
