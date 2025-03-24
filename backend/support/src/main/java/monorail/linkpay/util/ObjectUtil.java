package monorail.linkpay.util;

import static lombok.AccessLevel.PRIVATE;

import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ObjectUtil {

    public static boolean isNotNull(final Object o) {
        return !isNull(o);
    }

    public static boolean isNull(final Object o) {
        return Objects.isNull(o);
    }
}
