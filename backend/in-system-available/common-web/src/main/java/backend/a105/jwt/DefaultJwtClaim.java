package backend.a105.jwt;

public enum DefaultJwtClaim {
    TOKEN_ID("tokenId"),
    TOKEN_TYPE("type"),
    SALT("salt");

    private final String value;

    DefaultJwtClaim(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}