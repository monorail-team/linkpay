package monorail.linkpay.util.encoder;

import monorail.linkpay.exception.LinkPayException;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;

public class Base85Encoder {
    private static final int BASE = 85;
    private static final int ASCII_OFFSET = 33; // Z85 범위 시작 문자

    public static String encode(byte[] data) {
        int padding = (4 - (data.length % 4)) % 4;
        byte[] padded = Arrays.copyOf(data, data.length + padding);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < padded.length; i += 4) {
            long value = 0;
            for (int j = 0; j < 4; j++) {
                value = (value << 8) | (padded[i + j] & 0xFF);
            }

            char[] chunk = new char[5];
            for (int j = 4; j >= 0; j--) {
                chunk[j] = (char) ((value % BASE) + ASCII_OFFSET);
                value /= BASE;
            }
            sb.append(chunk);
        }

        return sb.toString();
    }

    public static byte[] decode(String encoded) {
        if (encoded.length() % 5 != 0) {
            throw new LinkPayException(INVALID_REQUEST, "Base85 문자열 길이는 5의 배수여야 합니다.");
        }

        int byteLength = (encoded.length() * 4) / 5;
        byte[] result = new byte[byteLength];
        int byteIndex = 0;

        for (int i = 0; i < encoded.length(); i += 5) {
            long value = 0;
            for (int j = 0; j < 5; j++) {
                value = value * BASE + (encoded.charAt(i + j) - ASCII_OFFSET);
            }

            for (int j = 3; j >= 0; j--) {
                result[byteIndex + j] = (byte) (value & 0xFF);
                value >>= 8;
            }
            byteIndex += 4;
        }

        return result;
    }

    public static String encodeLongToBase85(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        return Base85Encoder.encode(buffer.array());
    }


    public static long decodeBase85ToLong(String encoded) {
        byte[] bytes = Base85Encoder.decode(encoded);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }

}
