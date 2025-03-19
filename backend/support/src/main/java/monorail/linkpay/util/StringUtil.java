package monorail.linkpay.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.micrometer.common.util.StringUtils.isEmpty;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class StringUtil {

    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        } else if (separator == null) {
            return "";
        } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? "" : str.substring(pos + separator.length());
        }
    }
}
