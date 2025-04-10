package monorail.linkpay.algo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Reference: https://github.com/intotherealworld/jamo?tab=readme-ov-file
 */
public class JamoUtils {
    private JamoUtils() {
    }

    private static final int HANGLE_START = 0xAC00;
    private static final int HANGLE_END = 0xD79D;

    private static final  String [] CHOSUNG = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
            "ㅅ","ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    private static final String [] JUNGSUNG = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ",
            "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
    private static final String [] JONGSUNG = {"", "ㄱ", "ㄲ", "ᆪ", "ᆫ", "ᆬ", "ᆭ", "ㄷ",
            "ㄹ", "ᆰ", "ᆱ", "ᆲ", "ᆳ", "ᆴ", "ᆵ", "ᆶ", "ㅁ", "ㅂ", "ᆹ", "ᆺ", "ᆻ", "ᆼ",
            "ᆽ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    private static final Set<String> chosungSet = Arrays.stream(CHOSUNG).collect(Collectors.toSet());

    public static List<String> splitOne(String target) {
        int codePoint = Character.codePointAt(target, 0);
        if (codePoint >= HANGLE_START && codePoint <= HANGLE_END) {
            int idx = codePoint - HANGLE_START;
            // 유니코드에서 특정 글자의 코드 값은 ((초성 * 21) + 중성) * 28 + 종성
            int jong = idx % 28; // 종성 28개
            int jung = ((idx - jong) / 28) % 21; // 중성 21개
            int cho = (((idx - jong) / 28) - jung) / 21; // 초성 19개
            return List.of(CHOSUNG[cho], JUNGSUNG[jung], JONGSUNG[jong]);
        }

        return List.of(target);
    }

    public static boolean isChosungString(String str) {
        for (String s : str.split("")) {
            if(!isChosung(s) && !s.equals(" ") && !s.equals(",") && !s.equals(".")) return false;
        }
        return true;
    }

    private static boolean isChosung(String s){
        return chosungSet.contains(s);
    }

    public static List<List<String>> split(String target) {
        return Arrays.asList(target.split(""))
                .stream()
                .map(JamoUtils::splitOne)
                .collect(Collectors.toList());
    }
}
