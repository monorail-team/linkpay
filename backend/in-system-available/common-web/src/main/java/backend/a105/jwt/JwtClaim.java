package backend.a105.jwt;

public enum JwtClaim {
    TOKEN_ID("tokenId"),
    MEMBER_ID("memberId"),
    ROLES("roles"),
    TYPE("type"),
    SALT("salt");

    private final String value;

    JwtClaim(String value) {
        this.value = value;
    }

    String claim() {
        return value;
    }
}