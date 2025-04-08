package monorail.linkpay.linkcard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardColor {

    LIGHT_PINK("#ffeffa"),
    CHERRY_BLOSSOM("#F19DAB"),
    PEACH("#FEB8B9"),
    LIGHT_ORANGE("#fec9b8"),
    ORANGE("#ffba86"),
    YELLOW("#ffd681"),
    MELLON("#eeffba"),
    GREEN("#aedcb1"),
    LIGHT_GREEN("#c6f8d4"),
    MINT("#bcffe9"),
    AQUA("#b7f8ff"),
    SKY("#d8edfc"),
    LILAC("#dad8fc"),
    PURPLE("#d7c6e8"),
    PLUM("#d59ccf");

    private final String hexCode;

    public static CardColor getRandomColor() {
        return values()[(int) (Math.random() * values().length)];
    }
}
