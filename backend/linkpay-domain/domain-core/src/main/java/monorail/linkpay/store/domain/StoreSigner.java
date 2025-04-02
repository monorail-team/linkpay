package monorail.linkpay.store.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Table(name = "store_signer")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreSigner {
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
    private StoreSigner(Long id, Store store, byte[] encryptKey, byte[] decryptKey) {
        this.id = id;
        this.store = store;
        this.encryptKey = Base64.getEncoder().encodeToString(encryptKey);
        this.decryptKey = Base64.getEncoder().encodeToString(decryptKey);
    }
}


