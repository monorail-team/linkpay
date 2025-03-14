package backend.a105.util;

import static io.micrometer.common.util.StringUtils.isEmpty;

public class StringUtil {
    public static String substringAfter(String str, String separator) {
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
