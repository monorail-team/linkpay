package monorail.linkpay.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "transaction_signature_key")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionSignatureKey {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    @Lob
    private String encryptKey; // todo DB에 키 저장 시 암호화

    @Lob
    private String decryptKey;

    @Builder
    private TransactionSignatureKey(Long id, Store store, String encryptKey, String decryptKey) {
        this.id = id;
        this.store = store;
        this.encryptKey = encryptKey;
        this.decryptKey = decryptKey;
    }
}


