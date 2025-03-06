package backend.a105.auth;

import lombok.Builder;

@Builder
public record LoginCandidate(UserId userId, Email email) {
    public static LoginCandidate of() {
        return LoginCandidate.builder()
                .build();
    }
}
