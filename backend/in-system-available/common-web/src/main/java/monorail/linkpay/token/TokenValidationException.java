package monorail.linkpay.token;

public class TokenValidationException extends Exception {
    public TokenValidationException() {
        super("유효하지 않은 토큰입니다");
    }

    public TokenValidationException(String message) {
        super(message);
    }
}