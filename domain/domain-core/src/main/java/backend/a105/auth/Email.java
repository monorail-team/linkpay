package backend.a105.auth;

public record Email(String value) {
    public static Email of(String email) {
        return new Email(email);
    }
}
