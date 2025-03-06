package backend.a105.auth;

import lombok.Builder;

@Builder
public record LoginCandidate(MemberId memberId, Email email) {
    public static LoginCandidate of() {
        return LoginCandidate.builder()
                .build();
    }
}
