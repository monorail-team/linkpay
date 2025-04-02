package monorail.linkpay.auth.dto;

public record LoginPrincipal(String email, String username) {

    public static LoginPrincipal of(final String email, final String username) {
        return new LoginPrincipal(email, username);
    }
}
