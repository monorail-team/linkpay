package backend.a105.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record KakaoUserResponse(
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount // 중첩 객체
) {

    public static KakaoUserResponse of(String email) {
        return new KakaoUserResponse(new KakaoAccount(email));
    }

    @Builder
    public record KakaoAccount(String email) {
    }
}
