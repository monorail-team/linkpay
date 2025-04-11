package monorail.linkpay.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record KakaoUserResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public static KakaoUserResponse of(final String email, final String nickname) {
        return new KakaoUserResponse(new KakaoAccount(email, new KakaoProfile(nickname)));
    }

    @Builder
    public record KakaoAccount(
            @JsonProperty("email") String email,
            @JsonProperty("profile") KakaoProfile profile
    ) {}

    @Builder
    public record KakaoProfile(
            @JsonProperty("nickname") String nickname
    ) {}
}
