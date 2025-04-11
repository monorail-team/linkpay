package monorail.linkpay.util.encoder;

import java.util.Arrays;

public class Base91Encoder {

    private static final int BASE = 91;
    private static final char[] ENCODING_TABLE;
    private static final int[] DECODING_TABLE;

    static {
        // Base91 알파벳 (표준 printable ASCII 중 안정적인 문자들)
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                + "!#$%&()*+,./:;<=>?@[]^_`{|}~\""; // 총 91 문자
        ENCODING_TABLE = alphabet.toCharArray();
        DECODING_TABLE = new int[128];
        Arrays.fill(DECODING_TABLE, -1);
        for (int i = 0; i < ENCODING_TABLE.length; i++) {
            DECODING_TABLE[ENCODING_TABLE[i]] = i;
        }
    }

    public static String encode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int b = 0, n = 0;

        for (byte value : data) {
            b |= (value & 0xFF) << n;
            n += 8;

            if (n > 13) {
                int v = b & 8191;  // 2^13
                if (v > 88) {
                    b >>= 13;
                    n -= 13;
                } else {
                    v = b & 16383;  // 2^14
                    b >>= 14;
                    n -= 14;
                }
                sb.append(ENCODING_TABLE[v % BASE]);
                sb.append(ENCODING_TABLE[v / BASE]);
            }
        }

        if (n > 0) {
            sb.append(ENCODING_TABLE[b % BASE]);
            if (n > 7 || b > BASE) {
                sb.append(ENCODING_TABLE[b / BASE]);
            }
        }

        return sb.toString();
    }

    public static byte[] decode(String encoded) {
        byte[] buffer = new byte[encoded.length() * 2];  // worst case
        int b = 0, n = 0, outIndex = 0;
        int v = -1;

        for (int i = 0; i < encoded.length(); i++) {
            int c = encoded.charAt(i);
            if (c >= 128 || DECODING_TABLE[c] == -1) continue;

            if (v == -1) {
                v = DECODING_TABLE[c];
            } else {
                v += DECODING_TABLE[c] * BASE;
                b |= v << n;
                n += (v & 8191) > 88 ? 13 : 14;

                while (n >= 8) {
                    buffer[outIndex++] = (byte) (b & 0xFF);
                    b >>= 8;
                    n -= 8;
                }
                v = -1;
            }
        }

        if (v != -1) {
            b |= v << n;
            n += 7;
            while (n >= 8) {
                buffer[outIndex++] = (byte) (b & 0xFF);
                b >>= 8;
                n -= 8;
            }
        }

        return Arrays.copyOf(buffer, outIndex);
    }
}
