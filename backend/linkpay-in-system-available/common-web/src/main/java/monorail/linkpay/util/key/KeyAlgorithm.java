package monorail.linkpay.util.key;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeyAlgorithm {
    RSA("RSA", 2048),
    ED25519("Ed25519", null),
    ;

    private final String value;
    private final Integer keySize;
}
