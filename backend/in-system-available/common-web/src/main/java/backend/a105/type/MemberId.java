package backend.a105.type;

import jakarta.persistence.Embeddable;

@Embeddable
public record MemberId(long value) {
}
