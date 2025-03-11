package backend.a105.auth;

import lombok.Builder;

import java.util.List;

@Builder
public record AccessTokenPayload(long memberId,
                                 List<String> roles) {
}