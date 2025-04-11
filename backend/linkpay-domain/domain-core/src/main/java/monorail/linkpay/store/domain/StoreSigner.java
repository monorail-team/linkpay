package monorail.linkpay.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Base64;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StoreSigner that)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}


