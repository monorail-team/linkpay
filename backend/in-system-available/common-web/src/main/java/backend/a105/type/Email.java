package backend.a105.type;

public record Email(String value) {
    public static Email of(String email) {
        return new Email(email);
    }
}
