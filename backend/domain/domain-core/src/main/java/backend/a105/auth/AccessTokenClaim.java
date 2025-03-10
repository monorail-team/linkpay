package backend.a105.auth;

public enum AccessTokenClaim {
    MEMBER_ID("memberId"),
    ROLES("roles");

    private final String value;

    AccessTokenClaim(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}