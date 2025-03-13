package backend.a105.auth.dto;

public record LoginPrincipal(String email) {
    public static LoginPrincipal of(String email) {
        return new LoginPrincipal(email);
    }
}
