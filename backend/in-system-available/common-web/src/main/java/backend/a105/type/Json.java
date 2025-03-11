package backend.a105.type;

public record Json(String value) {
    public static Json of(String value) {
        return new Json(value);
    }
}
