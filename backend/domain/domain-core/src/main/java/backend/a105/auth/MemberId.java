package backend.a105.auth;

import jakarta.persistence.Embeddable;

@Embeddable
public record MemberId(long value) {
}
