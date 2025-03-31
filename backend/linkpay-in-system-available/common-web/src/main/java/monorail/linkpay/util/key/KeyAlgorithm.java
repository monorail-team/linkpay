package monorail.linkpay.util.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeyAlgorithm {
    RSA("RSA");

    private final String value;
}
