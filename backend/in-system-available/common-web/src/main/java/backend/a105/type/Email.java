package backend.a105.type;

import jakarta.persistence.Embeddable;

@Embeddable
public record Email(String value) {
    public static Email of(String email) {
        return new Email(email);
    }
}
