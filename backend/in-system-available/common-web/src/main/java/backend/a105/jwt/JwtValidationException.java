package backend.a105.jwt;

public class JwtValidationException extends Exception {
    public JwtValidationException() {
        super("유효하지 않은 JWT입니다");
    }

    public JwtValidationException(String message) {
        super(message);
    }
}