package monorail.linkpay.util;

import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ObjectUtil {

    public static boolean isNotNull(final Object o) {
        return !isNull(o);
    }

    public static boolean isNull(final Object o) {
        return Objects.isNull(o);
    }
}
