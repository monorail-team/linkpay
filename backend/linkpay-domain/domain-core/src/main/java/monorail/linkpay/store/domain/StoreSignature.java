package monorail.linkpay.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "store_signature")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreSignature {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    @Lob
    private String encryptKey;

    @Lob
    private String decryptKey;

    @Builder
    private StoreSignature(Long id, Store store, String encryptKey, String decryptKey) {
        this.id = id;
        this.store = store;
        this.encryptKey = encryptKey;
        this.decryptKey = decryptKey;
    }
}


