package backend.a105.util;

import java.util.Objects;

public class ObjectUtil {
    public static boolean isNotNull(Object o) {
        return !isNull(o);
    }

    public static boolean isNull(Object o) {
        return Objects.isNull(o);
    }
}
